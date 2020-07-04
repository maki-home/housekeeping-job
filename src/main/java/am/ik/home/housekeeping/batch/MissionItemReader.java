package am.ik.home.housekeeping.batch;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import am.ik.home.housekeeping.Mission;
import am.ik.home.housekeeping.client.MissionApi;
import am.ik.home.housekeeping.client.MissionResponse;

import org.springframework.batch.item.ItemReader;

public class MissionItemReader implements ItemReader<Mission> {
	private final MissionApi missionApi;

	private final BlockingDeque<MissionResponse> deque = new LinkedBlockingDeque<>();

	private static final MissionResponse POISON = new MissionResponse();

	public MissionItemReader(MissionApi missionApi) {
		this.missionApi = missionApi;
		this.missionApi.getMissions()
				.doOnNext(this.deque::addLast)
				.doOnTerminate(() -> this.deque.addLast(POISON))
				.subscribe();
	}

	@Override
	public Mission read() throws Exception {
		final MissionResponse response = this.deque.pollFirst(1, TimeUnit.HOURS);
		if (response == POISON || response == null) {
			return null;
		}
		return new Mission(response.getPlace(), response.getLastDate(), response.getCycle());
	}
}
