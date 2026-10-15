package pe.edu.upc.ayquipavalentino.security;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import pe.edu.upc.ayquipavalentino.exceptions.ApiError;
@Component
public class SecurityErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    private final ObjectMapper mapper;
    public SecurityErrorHandler(ObjectMapper mapper) { this.mapper = mapper; }
    @Override public void commence(HttpServletRequest request, HttpServletResponse response,
                                   AuthenticationException exception) throws IOException {
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
        write(request, response, 401, "Se requiere un token válido");
    }
    @Override public void handle(HttpServletRequest request, HttpServletResponse response,
                                AccessDeniedException exception) throws IOException {
        write(request, response, 403, "No tiene el rol requerido");
    }
    private void write(HttpServletRequest request, HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        mapper.writeValue(response.getOutputStream(), new ApiError(Instant.now(), status, message, request.getRequestURI(), Map.of()));
    }
}
