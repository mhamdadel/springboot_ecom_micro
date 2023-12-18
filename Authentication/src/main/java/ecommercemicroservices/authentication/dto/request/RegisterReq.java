package ecommercemicroservices.authentication.dto.request;

import lombok.Getter;

@Getter
public class RegisterReq {
    private String email;
    private String password;
}
