package pl.czajkowski.devconnect.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.czajkowski.devconnect.auth.models.LoginRequest;
import pl.czajkowski.devconnect.auth.models.LoginResponse;
import pl.czajkowski.devconnect.security.jwt.JwtTokenService;
import pl.czajkowski.devconnect.user.UserDTOMapper;
import pl.czajkowski.devconnect.user.models.User;

@Service
public class AuthService {

    private final JwtTokenService service;
    private final UserDetailsService userService;
    private final UserDTOMapper mapper;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            JwtTokenService service,
            UserDetailsService userService,
            UserDTOMapper mapper,
            AuthenticationManager authenticationManager) {
        this.service = service;
        this.userService = userService;
        this.mapper = mapper;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        return new LoginResponse(
                mapper.apply((User)userService.loadUserByUsername(request.email())),
                service.generateToken(auth)
        );
    }
}