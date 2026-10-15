package pe.edu.upc.ayquipavalentino.dtos;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record ZonaRequestDTO(
    @NotBlank(message = "El nombre es obligatorio") String name,
    @NotBlank(message = "La ubicación es obligatoria") String location,
    @NotNull(message = "El área es obligatoria") @Positive(message = "El área debe ser positiva") Double areaHectares,
    @NotNull(message = "La fecha de intervención es obligatoria") @JsonFormat(pattern = "dd/MM/yyyy") LocalDate interventionDate,
    @NotBlank(message = "El estado es obligatorio") String status
) {}
