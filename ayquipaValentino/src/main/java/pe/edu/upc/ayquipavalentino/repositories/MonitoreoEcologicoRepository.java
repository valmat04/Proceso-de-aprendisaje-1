package pe.edu.upc.ayquipavalentino.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.upc.ayquipavalentino.entities.MonitoreoEcologico;
import java.util.List;
public interface MonitoreoEcologicoRepository extends JpaRepository<MonitoreoEcologico, Integer> {
    @Query(value = """
        SELECT record_date AS "date", COUNT(*) AS cantidad
        FROM monitoreo_ecologico
        GROUP BY record_date
        ORDER BY record_date
        """, nativeQuery = true)
    List<MonitorCountProjection> countByRecordDate();
}
