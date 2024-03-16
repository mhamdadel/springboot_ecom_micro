package ecommercemicroservices.authentication.service;

import ecommercemicroservices.authentication.dto.request.RegisterReq;
import ecommercemicroservices.authentication.model.CustomUser;
import ecommercemicroservices.authentication.model.Role;
import ecommercemicroservices.authentication.repository.RoleRepository;
import ecommercemicroservices.authentication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private JwtEncoder jwtEncoder;

    @Value("${app.defaults.role}")
    private String defaultUserRole;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, JwtEncoder jwtEncoder, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtEncoder = jwtEncoder;
        this.jwtService = jwtService;
    }



    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
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
        newUser.setEnabled(true);
        newUser.setAccountNonExpired(true);
        newUser.setCredentialsNonExpired(true);
        newUser.setAccountNonLocked(true);

        Role defaultRole = roleRepository.findByName(defaultUserRole);
        if (defaultRole == null) {
            throw new RuntimeException("Default role not found");
        }

        newUser.setRoles(Collections.singleton(defaultRole));

        userRepository.save(newUser);

        return newUser;
    }
}
