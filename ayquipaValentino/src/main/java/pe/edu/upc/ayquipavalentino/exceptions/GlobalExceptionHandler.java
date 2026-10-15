package pe.edu.upc.ayquipavalentino.exceptions;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.*;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String,String> fields = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> fields.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ApiError(Instant.now(), 400,
            "Datos de entrada inválidos", request.getRequestURI(), fields));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiError> invalidJson(HttpServletRequest request) {
        return ResponseEntity.badRequest().body(new ApiError(Instant.now(), 400,
            "JSON inválido: revise tipos y fechas (dd/MM/yyyy)", request.getRequestURI(), Map.of()));
    }
    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ApiError> invalidCredentials(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(Instant.now(), 401,
            "Credenciales inválidas", request.getRequestURI(), Map.of()));
    }
}
