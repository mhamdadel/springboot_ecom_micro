package ecommercemicroservices.authentication.dto.response;


import lombok.Data;

@Data
public class RegisterRes {
    private String email;
    private String token;

    public RegisterRes(String email, String token) {
        this.email = email;
        this.token = token;
    }

    // Getters (you can generate these methods based on your IDE or manually)

    // No setters for simplicity, but you may include them if needed
}