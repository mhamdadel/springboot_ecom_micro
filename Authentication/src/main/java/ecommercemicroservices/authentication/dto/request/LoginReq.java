package ecommercemicroservices.authentication.dto.request;

import lombok.Data;

@Data
public class LoginReq {
    private String email;
    private String username;
    private String password;
}
