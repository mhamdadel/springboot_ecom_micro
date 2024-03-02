package ecommercemicroservices.authentication.service;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Jwt jwt;

    public JwtAuthenticationToken(Jwt jwt) {
        super(null);
        this.jwt = jwt;
    }

    public JwtAuthenticationToken(Jwt jwt, Collection<?> authorities) {
        super((Collection<? extends GrantedAuthority>) authorities);
        this.jwt = jwt;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.jwt;
    }
}
