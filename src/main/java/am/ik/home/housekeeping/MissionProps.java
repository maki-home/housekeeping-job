package am.ik.home.housekeeping;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "mission")
@ConstructorBinding
public class MissionProps {
	private final String apiUrl;

	private final String uiUrl;

	private final String mailTo;

	public MissionProps(String apiUrl, String uiUrl, String mailTo) {
		this.apiUrl = apiUrl;
		this.uiUrl = uiUrl;
		this.mailTo = mailTo;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public String getUiUrl() {
		return uiUrl;
	}

	public String getMailTo() {
		return mailTo;
	}
}
