package ecommercemicroservices.authentication.controller;

import ecommercemicroservices.authentication.dto.request.LoginReq;
import ecommercemicroservices.authentication.dto.request.RegisterReq;
import ecommercemicroservices.authentication.dto.response.LoginRes;
import ecommercemicroservices.authentication.dto.response.RegisterRes;
import ecommercemicroservices.authentication.error.exceptions.UnauthorizedException;
import ecommercemicroservices.authentication.model.CustomUser;
import ecommercemicroservices.authentication.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq loginReq) {
        try {
            String usernameOrEmail = loginReq.getUsername() != null ? loginReq.getUsername() : loginReq.getEmail();
            Authentication authentication =
                    authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(
                                    usernameOrEmail,
                                    loginReq.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("Token requested for user :{}", authentication.getAuthorities());
            String token = authService.generateToken(authentication);

            LoginRes response = new LoginRes(usernameOrEmail, token);

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid username or password" + e.getMessage());
        }
    }


    @PostMapping("/register")
    public ResponseEntity<RegisterRes> register(@RequestBody RegisterReq registerReq) {
        try {
            CustomUser newUser = authService.register(registerReq);
            String usernameOrEmail = registerReq.getUsername() != null ? registerReq.getUsername() : registerReq.getEmail();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameOrEmail, registerReq.getPassword())
            );

            String token = authService.generateToken(authentication);

            return ResponseEntity.ok(new RegisterRes(newUser.getEmail(), token));
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: User with this email already exists || " + e.getMessage());
        }
    }
}
