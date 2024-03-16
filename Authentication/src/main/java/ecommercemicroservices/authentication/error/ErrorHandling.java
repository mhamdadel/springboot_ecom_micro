package ecommercemicroservices.authentication.error;

import ecommercemicroservices.authentication.error.exceptions.ForbiddenException;
import ecommercemicroservices.authentication.error.exceptions.UnauthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;

@RestControllerAdvice
public class ErrorHandling {
    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(new Date(), ex.getMessage(), request.getDescription(false));
        // You can perform auditing here if required
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public final ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(new Date(), ex.getMessage(), request.getDescription(false));
        // You can perform auditing here if required
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ErrorResponse errorDetails = new ErrorResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, headers, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleForbiddenException(Exception ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(new Date(), ex.getMessage(), request.getDescription(false));
        // You can perform auditing here if required
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
