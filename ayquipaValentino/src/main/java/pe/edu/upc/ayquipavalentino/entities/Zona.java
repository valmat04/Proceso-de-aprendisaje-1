package pe.edu.upc.ayquipavalentino.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "zona")
public class Zona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idZona;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Double areaHectares;

    @Column(nullable = false)
    private LocalDate interventionDate;

    @Column(nullable = false)
    private String status;

    public Zona() {}

    public Integer getIdZona() { return idZona; }
    public void setIdZona(Integer idZona) { this.idZona = idZona; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getAreaHectares() { return areaHectares; }
    public void setAreaHectares(Double areaHectares) { this.areaHectares = areaHectares; }

    public LocalDate getInterventionDate() { return interventionDate; }
    public void setInterventionDate(LocalDate interventionDate) { this.interventionDate = interventionDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}
