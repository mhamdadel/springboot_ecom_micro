package ecommercemicroservices.authentication.model.redis;

import lombok.*;

import java.time.LocalDateTime;

//@RedisHash(value = "Tokens", timeToLive = 86400)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokensEntity {


    private String id;

    private String username;
    private String authenticationToken;
    private String modifiedBy;
    private LocalDateTime modifiedOn;
    private String createdBy;
    private LocalDateTime createdOn;
}