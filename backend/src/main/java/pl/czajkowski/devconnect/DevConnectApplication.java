package pl.czajkowski.devconnect;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.czajkowski.devconnect.security.jwt.RsaKeysProperties;
import pl.czajkowski.devconnect.technology.Technology;
import pl.czajkowski.devconnect.user.UserRepository;
import pl.czajkowski.devconnect.user.models.Role;
import pl.czajkowski.devconnect.user.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeysProperties.class)
public class DevConnectApplication  {

	public static void main(String[] args) {
		SpringApplication.run(DevConnectApplication.class, args);
	}
}
