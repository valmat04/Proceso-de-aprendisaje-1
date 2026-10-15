package pe.edu.upc.ayquipavalentino.config;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.ayquipavalentino.entities.*;
import pe.edu.upc.ayquipavalentino.repositories.*;
@Component
@ConditionalOnProperty(name = "app.demo.seed", havingValue = "true")
public class DemoDataConfig implements ApplicationRunner {
    private final ZonaRepository zones;
    private final EspecieRepository species;
    private final MonitoreoEcologicoRepository monitors;
    public DemoDataConfig(ZonaRepository zones, EspecieRepository species, MonitoreoEcologicoRepository monitors) {
        this.zones = zones; this.species = species; this.monitors = monitors;
    }
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // No modifica datos existentes ni duplica registros en siguientes arranques.
        if (zones.count() != 0 || species.count() != 0 || monitors.count() != 0) return;
        String[] names = {"Sector totoral", "Sector juncal", "Laguna norte", "Ribera sur", "Franja de amortiguamiento"};
        String[] common = {"Totora", "Junco", "Garza blanca", "Pato colorado", "Gallareta andina"};
        String[] scientific = {"Schoenoplectus californicus", "Schoenoplectus americanus", "Ardea alba", "Spatula cyanoptera", "Fulica ardesiaca"};
        List<Zona> created = new ArrayList<>();
        for (int i=0; i<5; i++) {
            Zona z = new Zona();
            z.setName(names[i]); z.setLocation("Humedal de intervención, sector " + (i+1));
            z.setAreaHectares(10.0 + i*2); z.setInterventionDate(LocalDate.of(2026,10,1).plusDays(i));
            z.setStatus("EN RESTAURACIÓN"); created.add(zones.save(z));
            Especie e = new Especie(); e.setCommonName(common[i]); e.setScientificName(scientific[i]);
            e.setConservationStatus("EN EVALUACIÓN LOCAL"); e.setEcologicalRole(i<2 ? "Refugio y filtración" : "Fauna del humedal");
            e.setZona(created.get(i)); species.save(e);
            MonitoreoEcologico m = new MonitoreoEcologico(); m.setZona(created.get(i));
            m.setRecordDate(LocalDate.of(2026,10,12).plusDays(i/2));
            m.setWaterQuality(6.5 + i*0.2); m.setVegetationCoverage(40.0 + i*5);
            m.setFaunaCount(12 + i*3); m.setObservations("Seguimiento demostrativo del sector " + (i+1));
            monitors.save(m);
        }
    }
}
