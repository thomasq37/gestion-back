package fr.qui.gestion.appart.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.qui.gestion.contact.Contact;
import fr.qui.gestion.frais.Frais;
import fr.qui.gestion.pays.Pays;
import fr.qui.gestion.periodlocation.PeriodLocation;
import fr.qui.gestion.user.appuser.AppUserDTO;
import lombok.Data;

@Data
public class AppartementForProprioDTO {
	private Long id;
    private LocalDate dateAchat;
    private int numero;
    private String adresse;
    private String codePostal;
    private String ville;
    private int nombrePieces;
    private double surface;
    private boolean balcon;
    private double prix;
    private Double fraisNotaireEtNegociation;
    private Double estimation;
    private String dpe;
    private String lastDPEUrl;
    private Pays pays;
    private double depensesNettes;
    private double revenusNets;
    private double rentabiliteNette;
    private double tauxVacanceLocative;
    private double moyenneBeneficesNetParMois;
    private double totalFraisGestion;
    private double totalHonorairesDeLoc;
    private double totalTravaux;
    private double totalChargesFixesHorsFrais;
    private List<Frais> fraisFixe;
    private List<Contact> contacts;
    private List<String> images;
    @JsonIgnore
    private List<PeriodLocation> periodLocation;
    private AppUserDTO appUser;
    private List<AppUserDTO> gestionnaires;

}


