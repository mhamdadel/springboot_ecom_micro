package ecommercemicroservices.authentication.dto.response;

import org.springframework.security.core.Authentication;

public class LoginRes {
    private String email;
    private Authentication token;

    public LoginRes(String email, Authentication token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Authentication getToken() {
        return token;
    }

    public void setToken(Authentication token) {
        this.token = token;
    }
}