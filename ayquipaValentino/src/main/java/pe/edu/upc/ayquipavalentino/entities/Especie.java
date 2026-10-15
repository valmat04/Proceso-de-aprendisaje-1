package pe.edu.upc.ayquipavalentino.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "especie")
public class Especie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEspecie;

    @Column(nullable = false)
    private String commonName;

    @Column(nullable = false)
    private String scientificName;

    @Column(nullable = false)
    private String conservationStatus;

    @Column(nullable = false)
    private String ecologicalRole;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_zona", nullable = false)
    private Zona zona;

    public Especie() {}

    public Integer getIdEspecie() { return idEspecie; }
    public void setIdEspecie(Integer idEspecie) { this.idEspecie = idEspecie; }

    public String getCommonName() { return commonName; }
    public void setCommonName(String commonName) { this.commonName = commonName; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getConservationStatus() { return conservationStatus; }
    public void setConservationStatus(String conservationStatus) { this.conservationStatus = conservationStatus; }

    public String getEcologicalRole() { return ecologicalRole; }
    public void setEcologicalRole(String ecologicalRole) { this.ecologicalRole = ecologicalRole; }

    public Zona getZona() { return zona; }
    public void setZona(Zona zona) { this.zona = zona; }

}
