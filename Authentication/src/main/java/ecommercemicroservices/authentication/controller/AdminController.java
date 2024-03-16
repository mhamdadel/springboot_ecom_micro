package ecommercemicroservices.authentication.controller;

import ecommercemicroservices.authentication.model.CustomUser;
import ecommercemicroservices.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> helloAdmin(){
        return ResponseEntity.ok("Hello Admin");
    }

    @PreAuthorize("hasAuthority('READ_AUTHORITY')")
    @GetMapping("/user")
    public Optional<CustomUser> helloUser(){
        Optional<CustomUser> user = userRepository.findByEmail("mohamed@gmail.com");
        return user;
    }

    @PreAuthorize("hasAuthority('READ_AUTHORITY')")
    @GetMapping("/usr")
    public Optional<CustomUser> awe(){
        Optional<CustomUser> user = userRepository.findByEmail("mohamed@gmail.com");
        return user;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/ur")
    public Optional<CustomUser> eqwed(){
        Optional<CustomUser> user = userRepository.findByEmail("mohamed@gmail.com");
        return user;
    }

    @GetMapping("/u")
    public Optional<CustomUser> user(){
        return userRepository.findByEmail("mohamed@gmail.com");
    }

    @GetMapping("/no")
    public String useqwer(){
        return "hello world";
    }
}
