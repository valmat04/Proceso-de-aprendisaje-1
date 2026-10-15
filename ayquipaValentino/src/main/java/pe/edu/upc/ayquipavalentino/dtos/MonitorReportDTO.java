package pe.edu.upc.ayquipavalentino.dtos;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
public record MonitorReportDTO(
    @JsonFormat(pattern = "dd/MM/yyyy") LocalDate date,
    // El enunciado muestra un espacio final en esta clave JSON.
    @JsonProperty("cantidad ") Long cantidad
) {}
