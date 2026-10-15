package pe.edu.upc.ayquipavalentino.controllers;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import pe.edu.upc.ayquipavalentino.dtos.*;
import pe.edu.upc.ayquipavalentino.serviceinterfaces.IZonaService;
@RestController
@RequestMapping("/zones")
public class ZonaController {
    private final IZonaService service;
    public ZonaController(IZonaService service) { this.service = service; }
    @PostMapping
    @Operation(summary = "HUB01: registrar una zona sin token")
    public ResponseEntity<ZonaResponseDTO> register(@Valid @RequestBody ZonaRequestDTO request) {
        ZonaResponseDTO response = service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
