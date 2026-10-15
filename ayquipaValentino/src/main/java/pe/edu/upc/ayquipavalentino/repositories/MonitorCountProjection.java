package pe.edu.upc.ayquipavalentino.repositories;
import java.time.LocalDate;
public interface MonitorCountProjection {
    LocalDate getDate();
    Long getCantidad();
}
