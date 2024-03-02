package ecommercemicroservices.authentication.service;

import ecommercemicroservices.authentication.dto.request.RegisterReq;
import ecommercemicroservices.authentication.model.CustomUser;
import ecommercemicroservices.authentication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service("authService")
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }



    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities()
                .stream()
                .map(authority -> ((SimpleGrantedAuthority) authority).getAuthority())
                .collect(Collectors.joining(" "));


        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public CustomUser register(RegisterReq registerReq) {
        if (userRepository.existsByEmail(registerReq.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        CustomUser newUser = new CustomUser();
        newUser.setEmail(registerReq.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerReq.getPassword()));
        newUser.setUsername(registerReq.getUsername());;
        newUser.setFirstName(registerReq.getFirstName());
        newUser.setLastName(registerReq.getLastName());

        userRepository.save(newUser);

        return newUser;
    }
}
