package ecommercemicroservices.authentication.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;


import jakarta.persistence.*;

@Data
@Entity
@Table(name = "auth_granted_authorities")
public class CustomGrantedAuthority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;

    public CustomGrantedAuthority(String authority) {
        this.authority = authority;
    }

    public CustomGrantedAuthority() {
        
    }
}
