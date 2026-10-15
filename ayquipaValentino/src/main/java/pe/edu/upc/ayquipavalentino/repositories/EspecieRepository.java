package pe.edu.upc.ayquipavalentino.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.ayquipavalentino.entities.Especie;
public interface EspecieRepository extends JpaRepository<Especie, Integer> {}
