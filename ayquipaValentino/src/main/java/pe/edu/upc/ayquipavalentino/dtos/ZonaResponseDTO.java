package pe.edu.upc.ayquipavalentino.dtos;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
public record ZonaResponseDTO(Integer idZona, String name, String location, Double areaHectares,
    @JsonFormat(pattern = "dd/MM/yyyy") LocalDate interventionDate, String status) {}
