package ecommercemicroservices.authentication.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;


import jakarta.persistence.*;

@Data
@Entity
@Table(name = "auth_granted_authorities")
@AllArgsConstructor
@NoArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;

    @Setter
    private String authority;
}
