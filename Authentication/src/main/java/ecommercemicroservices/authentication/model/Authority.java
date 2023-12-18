package ecommercemicroservices.authentication.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Set;

@Entity
@Table(name = "auth_authorities")
@Data
public class Authority implements GrantedAuthority {

    private static final long serialVersionUID = 620L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String role;

    @ManyToMany(mappedBy = "authorities")
    private Set<CustomUser> users;


    public Authority(String role) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.role = role;
    }

    public Authority() {

    }

    public String getAuthority() {
        return this.role;
    }

}
