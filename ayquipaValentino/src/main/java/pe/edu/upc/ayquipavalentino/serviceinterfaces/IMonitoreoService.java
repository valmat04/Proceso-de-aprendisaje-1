package pe.edu.upc.ayquipavalentino.serviceinterfaces;
import java.util.List;
import pe.edu.upc.ayquipavalentino.dtos.MonitorReportDTO;
public interface IMonitoreoService {
    List<MonitorReportDTO> countByDate();
}
