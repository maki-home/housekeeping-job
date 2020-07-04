package am.ik.home.housekeeping;

import java.time.Clock;

import org.springframework.batch.item.ItemProcessor;

public class MissionItemProcessor implements ItemProcessor<Mission, Mission> {
	private final Clock clock;

	public MissionItemProcessor(Clock clock) {
		this.clock = clock;
	}

	@Override
	public Mission process(Mission mission) throws Exception {
		if (mission.isOverdue(this.clock)) {
			return mission;
		}
		return null;
	}
}
