package ecommercemicroservices.authentication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Entity
@Table(name = "auth_authorities")
@Getter
@Setter
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private boolean role;

    @ManyToMany(mappedBy = "authorities")
    private Set<CustomUser> users;

    @Override
    public String getAuthority() {
        return this.getName();
    }
}
