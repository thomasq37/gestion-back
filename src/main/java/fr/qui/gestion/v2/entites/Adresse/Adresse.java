package fr.qui.gestion.v2.entites.Adresse;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.enumeration.Pays.Pays;
import fr.qui.gestion.v2.util.AbstractEntityWithMasqueId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Adresse extends AbstractEntityWithMasqueId {
    private String numero;
    private String voie;
    private String complementAdresse;
    private String codePostal;
    private String ville;
    @Enumerated(EnumType.STRING)
    private Pays pays;
    @OneToOne(mappedBy = "adresse")
    private Logement logement;
}
