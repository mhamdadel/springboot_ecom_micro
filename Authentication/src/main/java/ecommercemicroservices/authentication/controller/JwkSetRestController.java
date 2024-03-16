package ecommercemicroservices.authentication.controller;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import ecommercemicroservices.authentication.config.Jwt.RsaKeyConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api")
public class JwkSetRestController {

    @Autowired
    private RsaKeyConfigProperties rsaKeyConfigProperties;

    @GetMapping("/.well-known/jwks.json")
    public JWKSet keys() {
        RSAKey rsaKey = new RSAKey.Builder(rsaKeyConfigProperties.publicKey())
                .build();

        List<JWK> jwkList = Collections.singletonList(rsaKey);
        return new JWKSet(jwkList);
    }
}

