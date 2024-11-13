package fr.qui.gestion.appart;


import java.time.LocalDate;
import java.util.List;
import fr.qui.gestion.contact.Contact;
import fr.qui.gestion.frais.Frais;
import fr.qui.gestion.pays.Pays;
import fr.qui.gestion.periodlocation.PeriodLocation;
import fr.qui.gestion.user.appuser.AppUser;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Appartement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pays_id")
    private Pays pays;


    @Transient
    private double revenusNets;


    @Transient
    private double depensesNettes;


    @OneToMany(mappedBy = "appartement", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Frais> fraisFixe;


    @OneToMany(mappedBy = "appartement", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Contact> contacts;


    @ElementCollection
    @CollectionTable(name = "appartement_images", joinColumns = @JoinColumn(name = "appartement_id"))
    private List<String> images;


    @OneToMany(mappedBy = "appartement", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<PeriodLocation> periodLocation;


    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;


    @ManyToMany
    @JoinTable(
            name = "appartement_gestionnaires",
            joinColumns = @JoinColumn(name = "appartement_id"),
            inverseJoinColumns = @JoinColumn(name = "gestionnaire_id")
    )
    private List<AppUser> gestionnaires;
}
