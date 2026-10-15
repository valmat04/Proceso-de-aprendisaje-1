package pe.edu.upc.ayquipavalentino.controllers;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import pe.edu.upc.ayquipavalentino.dtos.MonitorReportDTO;
import pe.edu.upc.ayquipavalentino.serviceinterfaces.IMonitoreoService;
@RestController
public class MonitoreoController {
    private final IMonitoreoService service;
    public MonitoreoController(IMonitoreoService service) { this.service = service; }
    @GetMapping("/monitors")
    @Operation(summary = "HUB02: cantidad de monitoreos por fecha; rol INSPECTOR",
        security = @SecurityRequirement(name = "bearerAuth"))
    public List<MonitorReportDTO> countByDate() { return service.countByDate(); }
}
