package pl.czajkowski.devconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.czajkowski.devconnect.security.jwt.RsaKeysProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeysProperties.class)
public class DevConnectApplication {
	public static void main(String[] args) {
		SpringApplication.run(DevConnectApplication.class, args);
	}
}
