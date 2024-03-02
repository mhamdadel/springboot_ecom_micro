package ecommercemicroservices.authentication.dto.response;


import lombok.Getter;

@Getter
public class RegisterRes {
    private final String email;
    private final String token;

    public RegisterRes(String email, String token) {
        this.email = email;
        this.token = token;
    }
}