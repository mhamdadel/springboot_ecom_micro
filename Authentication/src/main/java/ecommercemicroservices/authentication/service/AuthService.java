package ecommercemicroservices.authentication.service;

import ecommercemicroservices.authentication.dto.request.ChangePasswordRequest;
import ecommercemicroservices.authentication.dto.request.LoginReq;
import ecommercemicroservices.authentication.dto.request.RegisterReq;
import ecommercemicroservices.authentication.dto.response.LoginRes;
import ecommercemicroservices.authentication.enums.TokenType;
import ecommercemicroservices.authentication.model.CustomUser;
import ecommercemicroservices.authentication.model.Role;
import ecommercemicroservices.authentication.model.Token;
import ecommercemicroservices.authentication.repository.RoleRepository;
import ecommercemicroservices.authentication.repository.TokenRepository;
import ecommercemicroservices.authentication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private JwtEncoder jwtEncoder;

    @Value("${app.defaults.role}")
    private String defaultUserRole;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, JwtEncoder jwtEncoder, JwtService jwtService, TokenRepository tokenRepository, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtEncoder = jwtEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
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

    private void saveUserToken(CustomUser user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public LoginRes login(LoginReq loginReq) {
        String usernameOrEmail = loginReq.getUsername() != null ? loginReq.getUsername() : loginReq.getEmail();
        Optional<CustomUser> userOptional = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        CustomUser user = userOptional.orElseThrow(() -> new BadCredentialsException("Invalid username or email"));
        revokeAllUserTokens(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usernameOrEmail, loginReq.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication);

        return new LoginRes(user.getUsername(), token);
    }

    private void revokeAllUserTokens(CustomUser user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (CustomUser) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Passwords are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
    }

}
