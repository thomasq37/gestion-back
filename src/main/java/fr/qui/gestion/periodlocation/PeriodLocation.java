package fr.qui.gestion.periodlocation;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.frais.Frais;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    private String locataire;
    @Column(name = "is_loc_vac")
    private boolean isLocVac;
    @Column(name = "est_entree")
    private LocalDate estEntree;
    @Column(name = "est_sortie")
    private LocalDate estSortie;
    @OneToMany(mappedBy = "periodLocation", cascade = CascadeType.REMOVE)
    private List<Frais> frais;
}
