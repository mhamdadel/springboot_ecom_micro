package ecommercemicroservices.authentication.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "auth_users")
@Data
@NoArgsConstructor
public class User {

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

    @Size(max = 255, message = "Last name length must be less than or equal to 255 characters")
    private String lastName;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
