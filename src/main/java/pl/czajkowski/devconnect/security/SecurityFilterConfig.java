package pl.czajkowski.devconnect.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityFilterConfig {

    @Bean
    public SecurityFilterChain filter(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                            .requestMatchers(
                                    "/api/users/register",
                                    "/api/auth/login",
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**"
                            ).permitAll()
                            .anyRequest().authenticated()
                )
                .oauth2ResourceServer(
                        oauth2 -> oauth2
                            .jwt(Customizer.withDefaults())
                )
                .sessionManagement(
                        session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }
}
