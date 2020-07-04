package am.ik.home.housekeeping.batch;

import java.time.LocalDate;
import java.util.List;

import am.ik.home.housekeeping.Mission;
import am.ik.home.housekeeping.MissionProps;
import am.ik.home.sendgrid.SendGridSender;

import org.springframework.batch.item.ItemWriter;

public class MissionItemWriter implements ItemWriter<Mission> {
	private final MissionProps props;

	private final SendGridSender sendGridSender;

	public MissionItemWriter(MissionProps props, SendGridSender sendGridSender) {
		this.props = props;
		this.sendGridSender = sendGridSender;
	}

	@Override
	public void write(List<? extends Mission> list) throws Exception {
		final String subject = LocalDate.now() + "の掃除場所";
		final StringBuilder content = new StringBuilder("🧹 本日の掃除場所 🧹").append(System.lineSeparator());
		for (Mission mission : list) {
			content.append("■ ").append(mission.getPlace()).append(" (期日: ").append(mission.dueDate()).append(")").append(System.lineSeparator());
		}
		content.append(System.lineSeparator()).append("掃除が完了したら、 ").append(this.props.getUiUrl()).append(" に登録してください。");
		this.sendGridSender.sendMail(this.props.getMailTo(), subject, content.toString());
	}
}
