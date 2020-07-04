package am.ik.home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class HousekeepingJobApplication {

	public static void main(String[] args) {
		SpringApplication.run(HousekeepingJobApplication.class, args);
	}

}
