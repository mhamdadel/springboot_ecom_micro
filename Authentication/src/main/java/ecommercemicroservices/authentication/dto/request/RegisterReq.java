package ecommercemicroservices.authentication.dto.request;

import lombok.Getter;

@Getter
public class RegisterReq {
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
}
