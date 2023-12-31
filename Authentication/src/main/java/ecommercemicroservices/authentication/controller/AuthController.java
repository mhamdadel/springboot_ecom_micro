package ecommercemicroservices.authentication.controller;

import ecommercemicroservices.authentication.dto.request.LoginReq;
import ecommercemicroservices.authentication.dto.response.ErrorRes;
import ecommercemicroservices.authentication.dto.response.LoginRes;
import ecommercemicroservices.authentication.model.User;
import ecommercemicroservices.authentication.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/rest/auth")
public class AuthController {

    @Value("${jwt.secret}")
    private String jwtSecret;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository repository;

    @Autowired
    public AuthController(AuthService authService, AuthenticationManager authenticationManager,
                          SecurityContextRepository repository) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.repository = repository;
    }

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody LoginReq loginReq, HttpServletRequest req, HttpServletResponse res) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword())
            );
            String email = authentication.getName();
            User user = new User(email, "");

            String token = Jwts.builder()
                    .setSubject(email)
                    .signWith(SignatureAlgorithm.HS256, jwtSecret)
                    .compact();

            LoginRes loginRes = new LoginRes(email, token);

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()));
            repository.saveContext(context, req, res);

            return ResponseEntity.ok(loginRes);

        } catch (BadCredentialsException e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.UNAUTHORIZED, "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
