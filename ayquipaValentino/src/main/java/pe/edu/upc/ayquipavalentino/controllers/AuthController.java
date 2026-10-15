package pe.edu.upc.ayquipavalentino.controllers;
import jakarta.validation.Valid;
import java.time.Instant;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.ayquipavalentino.dtos.*;
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager manager;
    private final JwtEncoder encoder;
    public AuthController(AuthenticationManager manager, JwtEncoder encoder) {
        this.manager = manager; this.encoder = encoder;
    }
    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        Authentication auth = manager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder().subject(auth.getName()).issuedAt(now)
            .expiresAt(now.plusSeconds(3600))
            .claim("roles", auth.getAuthorities().stream()
                .map(a -> a.getAuthority().replaceFirst("^ROLE_", "")).toList()).build();
        String token = encoder.encode(JwtEncoderParameters.from(
            JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
        return new LoginResponseDTO(token, "Bearer", 3600);
    }
}
