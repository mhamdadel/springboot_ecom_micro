package ecommercemicroservices.authentication.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
class ErrorResponse {
    private Date timestamp;
    private String message;
    private String details;
}