package ecommercemicroservices.authentication.controller;

import ecommercemicroservices.authentication.dto.request.LoginReq;
import ecommercemicroservices.authentication.dto.request.RegisterReq;
import ecommercemicroservices.authentication.dto.response.LoginRes;
import ecommercemicroservices.authentication.dto.response.RegisterRes;
import ecommercemicroservices.authentication.error.exceptions.UnauthorizedException;
import ecommercemicroservices.authentication.model.CustomUser;
import ecommercemicroservices.authentication.repository.UserRepository;
import ecommercemicroservices.authentication.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

@Value("${jwt.secret}")
private String jwtSecret;

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRes> login(@RequestBody LoginReq loginReq) {
        try {
            String usernameOrEmail = loginReq.getUsername() != null ? loginReq.getUsername() : loginReq.getEmail();
            System.out.println(usernameOrEmail);
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameOrEmail, loginReq.getPassword())
            );
            String token = Jwts.builder()
                    .setSubject(usernameOrEmail)
                    .signWith(SignatureAlgorithm.HS256, jwtSecret)
                    .compact();
            return ResponseEntity.ok(new LoginRes(usernameOrEmail, token));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid username or password" + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterRes> register(@RequestBody RegisterReq registerReq) {
        try {
            CustomUser newUser = authService.register(registerReq);
            String token = Jwts.builder()
                    .setSubject(newUser.getEmail())
                    .signWith(SignatureAlgorithm.HS256, jwtSecret)
                    .compact();
            return ResponseEntity.ok(new RegisterRes(newUser.getEmail(), token));
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: User with this email already exists || " + e.getMessage());
        }
    }
}
