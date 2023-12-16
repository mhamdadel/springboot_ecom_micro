package ecommercemicroservices.authentication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "auth_users")
@AllArgsConstructor
@NoArgsConstructor
public class CustomUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(unique = true, nullable = false)
    @NotNull(message = "Email cannot be null")
    @Size(min = 1, max = 255, message = "Email length must be between 1 and 255 characters")
    @Getter
    @Setter
    private String email;

    @Column(nullable = false)
    @NotNull(message = "Password cannot be null")
    @Size(min = 1, max = 255, message = "Password length must be between 1 and 255 characters")
    @Getter
    @Setter
    private String password;

    @Size(max = 255, message = "First name length must be less than or equal to 255 characters")
    @Getter
    @Setter
    private String firstName;

    @Size(max = 255, message = "Last name length must be less than or equal to 255 characters")
    @Getter
    @Setter
    private String lastName;

    @ManyToMany
    @JoinTable(name="users_authorities",
            joinColumns=
            @JoinColumn(name="user_id", referencedColumnName="id"),
            inverseJoinColumns=
            @JoinColumn(name="authority_id", referencedColumnName="id")
    )
    private Set<Authority> authorities;

    @Getter
    @Setter
    private boolean accountNonExpired;

    @Getter
    @Setter
    private boolean accountNonLocked;

    @Getter
    @Setter
    private boolean credentialsNonExpired;

    @Getter
    @Setter
    private boolean enabled;

    public CustomUser(CustomUser customUser) {
        this.id = customUser.getId();
        this.email = customUser.getEmail();
        this.password = customUser.getPassword();
        this.firstName = customUser.getFirstName();
        this.lastName = customUser.getLastName();
        this.authorities = (Set<Authority>) customUser.getAuthorities();
        this.accountNonExpired = customUser.isAccountNonExpired();
        this.accountNonLocked = customUser.isAccountNonLocked();
        this.credentialsNonExpired = customUser.isCredentialsNonExpired();
        this.enabled = customUser.isEnabled();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Authority> authorities = this.authorities;
        List<GrantedAuthority> roleList = new ArrayList<>();

        for (var a : authorities) {
            if (a.isRole()) {
                roleList.add(new SimpleGrantedAuthority("ROLE_" + a.getName()));
            } else {
                roleList.add(new SimpleGrantedAuthority(a.getName()));
            }
        }
        return roleList;
    }

    @Override
    public String getUsername() {
        return this.email;
    }


}
