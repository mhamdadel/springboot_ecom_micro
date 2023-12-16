package ecommercemicroservices.authentication.service;

import ecommercemicroservices.authentication.dto.response.LoginRes;
import ecommercemicroservices.authentication.model.CustomUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;

@Service("authService")
public class AuthService {
    private PasswordEncoder passwordEncoder;
    public LoginRes signIn(CustomUser user) {
        if (passwordEncoder.matches(CharBuffer.wrap(user.getPassword()), "lLDINGz0YLUUFQuuj5ChAsq0GNM9yHeUAJiL2Be7WUh43Xo3gmXNaw==") ) {
            return new LoginRes(user.getEmail(), "123");
        }
        throw  new RuntimeException("Invalid Password");
    }
}
