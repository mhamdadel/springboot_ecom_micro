package ecommercemicroservices.authentication.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "auth_users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotNull(message = "Email cannot be null")
    @Size(min = 1, max = 255, message = "Email length must be between 1 and 255 characters")
    private String email;

    @Column(nullable = false)
    @NotNull(message = "Password cannot be null")
    @Size(min = 1, max = 255, message = "Password length must be between 1 and 255 characters")
    private String password;

    @Size(max = 255, message = "First name length must be less than or equal to 255 characters")
    private String firstName;

    @Size(max = 255, message = "First name length must be less than or equal to 255 characters")
    private String username;

    @Size(max = 255, message = "Last name length must be less than or equal to 255 characters")
    private String lastName;

    @ManyToMany
    @JoinTable(
            joinColumns=
            @JoinColumn(name="user_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="authority_id", referencedColumnName="id")
    )
    private Set<Authority> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    public CustomUser(CustomUser customUser) {
        this.id = customUser.getId();
        this.email = customUser.getEmail();
        this.password = customUser.getPassword();
        this.firstName = customUser.getFirstName();
        this.lastName = customUser.getLastName();
        this.authorities = customUser.getAuthorities();
        this.accountNonExpired = customUser.isAccountNonExpired();
        this.accountNonLocked = customUser.isAccountNonLocked();
        this.credentialsNonExpired = customUser.isCredentialsNonExpired();
        this.enabled = customUser.isEnabled();
    }


    @Override
    public Set<Authority> getAuthorities() {
        Set<Authority> authorities = this.authorities;
        Set<Authority> roleSet = new HashSet<>();

        for (var a : authorities) {
            roleSet.add(new Authority("ROLE_" + a.getName()));
        }
        return roleSet;
    }


    @Override
    public String getUsername() {
        return this.email;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;


}
