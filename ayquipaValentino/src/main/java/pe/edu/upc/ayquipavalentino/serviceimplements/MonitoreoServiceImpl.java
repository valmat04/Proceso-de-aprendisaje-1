package pe.edu.upc.ayquipavalentino.serviceimplements;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.ayquipavalentino.dtos.MonitorReportDTO;
import pe.edu.upc.ayquipavalentino.repositories.MonitoreoEcologicoRepository;
import pe.edu.upc.ayquipavalentino.serviceinterfaces.IMonitoreoService;
@Service
public class MonitoreoServiceImpl implements IMonitoreoService {
    private final MonitoreoEcologicoRepository repository;
    public MonitoreoServiceImpl(MonitoreoEcologicoRepository repository) { this.repository = repository; }
    @Override
    @Transactional(readOnly = true)
    public List<MonitorReportDTO> countByDate() {
        return repository.countByRecordDate().stream()
            .map(row -> new MonitorReportDTO(row.getDate(), row.getCantidad())).toList();
    }
}
