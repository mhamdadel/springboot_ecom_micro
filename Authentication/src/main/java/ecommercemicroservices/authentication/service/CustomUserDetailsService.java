package ecommercemicroservices.authentication.service;

import ecommercemicroservices.authentication.model.CustomGrantedAuthority;
import ecommercemicroservices.authentication.model.CustomUser;
import ecommercemicroservices.authentication.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        try {
            CustomUser user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username or Email"));

            Set<GrantedAuthority> authorities = user.getRoles().stream()
                    .map((role) -> new CustomGrantedAuthority(role.getName()))
                    .collect(Collectors.toSet());

            return new User(
                    usernameOrEmail,
                    user.getPassword(),
                    authorities
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}