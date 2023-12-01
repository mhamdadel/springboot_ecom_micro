package ecommercemicroservices.inventory.exception;

import java.time.LocalDateTime;

public record ApiError(StringBuffer path, String message, Integer statusCode, LocalDateTime occured_at) {

}
