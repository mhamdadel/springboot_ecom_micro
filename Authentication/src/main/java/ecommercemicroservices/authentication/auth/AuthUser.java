package ecommercemicroservices.authentication.auth;

import ecommercemicroservices.authentication.model.CustomUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public class AuthUser extends CustomUser implements UserDetails {

    private final CustomUser user;

    public AuthUser(CustomUser user) {
        this.user = user;
    }

    public CustomUser getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public Set<SimpleGrantedAuthority> getAuthorities() {
        return null;

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
