package am.ik.home.housekeeping;

import java.time.Clock;
import java.time.LocalDate;

public class Mission {
	final String place;

	final LocalDate lastDate;

	final Integer cycle;

	public Mission(String place, LocalDate lastDate, Integer cycle) {
		this.place = place;
		this.lastDate = lastDate;
		this.cycle = cycle;
	}

	public LocalDate dueDate() {
		return this.lastDate.plusDays(this.cycle);
	}

	public boolean isOverdue(Clock clock) {
		if (this.lastDate == null) {
			return false;
		}
		final LocalDate now = LocalDate.now(clock);
		return now.isAfter(this.dueDate());
	}

	@Override
	public String toString() {
		return "Mission{" +
				"place='" + place + '\'' +
				", lastDate=" + lastDate +
				", cycle=" + cycle +
				'}';
	}
}
