package fr.qui.gestion.periodlocation;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.frais.Frais;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class PeriodLocation {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appartement_id")
    @JsonIgnore
    private Appartement appartement;
    
    @Column(name = "prix")
    private double prix;

    // si loc vac le prix est par jour
    @Column(name = "is_loc_vac")
    private boolean isLocVac;

    
    @Transient
    public Long getAppartementId() {
        return (this.appartement != null) ? this.appartement.getId() : null;
    }
    
    @Column(name = "est_entree")
    private LocalDate estEntree;

    @Column(name = "est_sortie")
    private LocalDate estSortie;
    
    @OneToMany(mappedBy = "periodLocation")
    private List<Frais> frais;
}
