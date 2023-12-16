package ecommercemicroservices.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "auth_granted_authorities")
@AllArgsConstructor
@NoArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String authority;

    public CustomGrantedAuthority(String roleName) {
    }
}
