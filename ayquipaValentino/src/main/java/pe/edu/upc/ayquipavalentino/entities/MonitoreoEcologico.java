package pe.edu.upc.ayquipavalentino.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "monitoreo_ecologico")
public class MonitoreoEcologico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMonitoreo;

    @Column(nullable = false)
    private LocalDate recordDate;

    @Column(nullable = false)
    private Double waterQuality;

    @Column(nullable = false)
    private Double vegetationCoverage;

    @Column(nullable = false)
    private Integer faunaCount;

    @Column(nullable = false)
    private String observations;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_zona", nullable = false)
    private Zona zona;

    public MonitoreoEcologico() {}

    public Integer getIdMonitoreo() { return idMonitoreo; }
    public void setIdMonitoreo(Integer idMonitoreo) { this.idMonitoreo = idMonitoreo; }

    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }

    public Double getWaterQuality() { return waterQuality; }
    public void setWaterQuality(Double waterQuality) { this.waterQuality = waterQuality; }

    public Double getVegetationCoverage() { return vegetationCoverage; }
    public void setVegetationCoverage(Double vegetationCoverage) { this.vegetationCoverage = vegetationCoverage; }

    public Integer getFaunaCount() { return faunaCount; }
    public void setFaunaCount(Integer faunaCount) { this.faunaCount = faunaCount; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public Zona getZona() { return zona; }
    public void setZona(Zona zona) { this.zona = zona; }

}
