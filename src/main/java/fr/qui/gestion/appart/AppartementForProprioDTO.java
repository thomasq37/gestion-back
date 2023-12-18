package fr.qui.gestion.appart;

import java.util.List;

import fr.qui.gestion.contact.Contact;
import fr.qui.gestion.frais.Frais;
import fr.qui.gestion.periodlocation.PeriodLocation;
import fr.qui.gestion.user.AppUserDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class AppartementForProprioDTO {
	private Long id;
    private int numero;
    private String adresse;
    private String codePostal;
    private String ville;
    private int nombrePieces;
    private double surface;
    private boolean balcon;
    private double prix;
    @Enumerated(EnumType.STRING)
    private Pays pays;
    private double rentabiliteNette;
    private double tauxVacanceLocative;
    private double moyenneBeneficesNetParMois;
    private List<Frais> fraisFixe;
    private List<Contact> contacts;
    private List<String> images;
    private List<PeriodLocation> periodLocation;
    private AppUserDTO appUser;
    private List<AppUserDTO> gestionnaires;

}


