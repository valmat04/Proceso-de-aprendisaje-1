package pe.edu.upc.ayquipavalentino.serviceimplements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.ayquipavalentino.dtos.*;
import pe.edu.upc.ayquipavalentino.entities.Zona;
import pe.edu.upc.ayquipavalentino.repositories.ZonaRepository;
import pe.edu.upc.ayquipavalentino.serviceinterfaces.IZonaService;
@Service
public class ZonaServiceImpl implements IZonaService {
    private final ZonaRepository repository;
    public ZonaServiceImpl(ZonaRepository repository) { this.repository = repository; }
    @Override
    @Transactional
    public ZonaResponseDTO register(ZonaRequestDTO request) {
        Zona zona = new Zona();
        zona.setName(request.name());
        zona.setLocation(request.location());
        zona.setAreaHectares(request.areaHectares());
        zona.setInterventionDate(request.interventionDate());
        zona.setStatus(request.status());
        Zona saved = repository.save(zona);
        return new ZonaResponseDTO(saved.getIdZona(), saved.getName(), saved.getLocation(),
            saved.getAreaHectares(), saved.getInterventionDate(), saved.getStatus());
    }
}
