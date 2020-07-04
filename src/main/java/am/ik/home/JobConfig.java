package am.ik.home;

import java.time.Clock;

import am.ik.home.housekeeping.Mission;
import am.ik.home.housekeeping.MissionProps;
import am.ik.home.housekeeping.batch.MissionItemProcessor;
import am.ik.home.housekeeping.batch.MissionItemReader;
import am.ik.home.housekeeping.batch.MissionItemWriter;
import am.ik.home.housekeeping.client.ApiClient;
import am.ik.home.housekeeping.client.MissionApi;
import am.ik.home.sendgrid.SendGridSender;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobConfig {

	private final JobBuilderFactory jobBuilderFactory;

	private final StepBuilderFactory stepBuilderFactory;

	private final MissionProps missionProps;

	private final SendGridSender sendGridSender;

	public JobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, MissionProps missionProps, SendGridSender sendGridSender) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.missionProps = missionProps;
		this.sendGridSender = sendGridSender;
	}

	@Bean
	public MissionApi missionApi() {
		return new MissionApi(new ApiClient().setBasePath(this.missionProps.getApiUrl()));
	}

	@Bean
	public Step notifyMission() {
		return this.stepBuilderFactory.get("notifyMission")
				.<Mission, Mission>chunk(30)
				.reader(new MissionItemReader(this.missionApi()))
				.processor(new MissionItemProcessor(Clock.systemDefaultZone()))
				.writer(new MissionItemWriter(this.missionProps, this.sendGridSender))
				.build();
	}

	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("job")
				.incrementer(new RunIdIncrementer())
				.start(this.notifyMission())
				.build();
	}
}
