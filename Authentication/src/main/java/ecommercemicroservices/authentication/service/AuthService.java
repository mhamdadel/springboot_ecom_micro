package ecommercemicroservices.authentication.service;

import ecommercemicroservices.authentication.dto.request.RegisterReq;
import ecommercemicroservices.authentication.model.CustomUser;
import ecommercemicroservices.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("authService")
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

//    public LoginRes signIn(CustomUser user) {
//        if (passwordEncoder.matches(CharBuffer.wrap(user.getPassword()), "lLDINGz0YLUUFQuuj5ChAsq0GNM9yHeUAJiL2Be7WUh43Xo3gmXNaw==") ) {
//            return new LoginRes(user.getEmail(), "123");
//        }
//        throw  new RuntimeException("Invalid Password");
//    }

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
