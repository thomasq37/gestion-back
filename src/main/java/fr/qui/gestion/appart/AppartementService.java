package fr.qui.gestion.appart;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.qui.gestion.appart.dto.*;
import fr.qui.gestion.utilisateur.Utilisateur;
import fr.qui.gestion.utilisateur.UtilisateurDTO;
import fr.qui.gestion.utilisateur.UtilisateurRepository;
import fr.qui.gestion.utilisateur.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.qui.gestion.frais.Frais;
import fr.qui.gestion.frais.FraisRepository;
import fr.qui.gestion.periodlocation.PeriodLocation;
import fr.qui.gestion.periodlocation.PeriodLocationRepository;
import jakarta.transaction.Transactional;

@Service
public class AppartementService {
    @Autowired
    private FraisRepository fraisRepository;
    
    @Autowired
    private PeriodLocationRepository periodLocationRepository;
    
    @Autowired
    private AppartementRepository appartementRepository;
    
    @Autowired
    private UtilisateurRepository userRepository;

	private final UtilisateurService appUserService;
	private final AppartementCalculService appartementCalculService;

    public AppartementService(
			UtilisateurService appUserService,
			AppartementCalculService appartementCalculService) {
        this.appUserService = appUserService;
		this.appartementCalculService = appartementCalculService;
    }

    public Appartement ajouterAppartement(Appartement nouvelAppartement, String email) {
		Utilisateur utilisateur = userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

		// Associer l'utilisateur à l'Appartement
		nouvelAppartement.setAppUser(utilisateur);
        return appartementRepository.save(nouvelAppartement);
    }
    
    public Appartement mettreAJourUnAppartementPourUtilisateur(String email, Long appartId, Appartement appartementModifie) {
        Appartement appartementExist = obtenirUnAppartementParIdAndByUtilisateurId(appartId, email);
		Optional<Utilisateur> utilisateur = userRepository.findByEmail(email);
		if (utilisateur.isPresent() && !Objects.equals(appartementExist.getAppUser().getId(), utilisateur.get().getId())) {
            throw new SecurityException("L'utilisateur n'est pas autorisé à mettre à jour cet appartement.");
        }
		appartementExist.setDateAchat(appartementModifie.getDateAchat());
		appartementExist.setNumero(appartementModifie.getNumero());
        appartementExist.setAdresse(appartementModifie.getAdresse());
        appartementExist.setCodePostal(appartementModifie.getCodePostal());
        appartementExist.setVille(appartementModifie.getVille());
        appartementExist.setNombrePieces(appartementModifie.getNombrePieces());
        appartementExist.setSurface(appartementModifie.getSurface());
        appartementExist.setBalcon(appartementModifie.isBalcon());
        appartementExist.setPrix(appartementModifie.getPrix());
		appartementExist.setFraisNotaireEtNegociation(appartementModifie.getFraisNotaireEtNegociation());
		appartementExist.setEstimation(appartementModifie.getEstimation());
		appartementExist.setPays(appartementModifie.getPays());
        appartementExist.setDpe(appartementModifie.getDpe());
		appartementExist.setLastDPEUrl(appartementModifie.getLastDPEUrl());
        appartementExist.setImages(appartementModifie.getImages());
        appartementExist.setPays(appartementModifie.getPays());
        return appartementRepository.save(appartementExist);
    }
    
    public void supprimerUnAppartement(Long id) {
    	appartementRepository.deleteById(id);
    }
    
    // Frais
    
	@Transactional
	public void supprimerTousLesFraisParAppartementId(Long appartementId) {
       fraisRepository.deleteAllByAppartementId(appartementId);
   }
	
	@Transactional
	public Frais ajouterUnFraisPourPeriode(Long periodeId, Frais newFrais) {
		PeriodLocation periodLocation = periodLocationRepository.findById(periodeId)
				.orElseThrow(() -> new RuntimeException("PeriodLocation not found"));

        newFrais.setPeriodLocation(periodLocation);
        return fraisRepository.save(newFrais);
	}

	
	public Frais mettreAJourUnFraisPourPeriode(Long periodeId, Long fraisId, Frais fraisMisAJour) {
		Frais fraisActuel = fraisRepository.findById(fraisId)
              .orElseThrow(() -> new RuntimeException("Frais non trouvé"));
        if (!fraisActuel.getPeriodLocation().getId().equals(periodeId)) {
            throw new RuntimeException("Le frais n'appartient pas à la période donnée");
        }
        fraisActuel.setMontant(fraisMisAJour.getMontant());
        fraisActuel.setFrequence(fraisMisAJour.getFrequence());
        fraisActuel.setTypeFrais(fraisMisAJour.getTypeFrais());
        return fraisRepository.save(fraisActuel);
	}

	public void supprimerFraisPourPeriode(Long periodeId, Long fraisId) {
		Frais frais = fraisRepository.findById(fraisId)
                .orElseThrow(() -> new RuntimeException("Frais non trouvé"));

        if (!frais.getPeriodLocation().getId().equals(periodeId)) {
            throw new RuntimeException("Le frais n'appartient pas à la période donnée");
        }
        fraisRepository.delete(frais);
		
	}
	
	public AppartementForGestionDTO convertToDTO(Appartement appartement) {
		AppartementForGestionDTO dto = new AppartementForGestionDTO();
		dto.setId(appartement.getId());
		dto.setNumero(appartement.getNumero());
	    dto.setAdresse(appartement.getAdresse());
	    dto.setCodePostal(appartement.getCodePostal());
	    dto.setVille(appartement.getVille());
	    dto.setNombrePieces(appartement.getNombrePieces());
	    dto.setSurface(appartement.getSurface());
	    dto.setBalcon(appartement.isBalcon());
	    dto.setPays(appartement.getPays());
	    dto.setImages(appartement.getImages());
	    dto.setPeriodLocation(appartement.getPeriodLocation());
	    dto.setAppartProprioUsername(appartement.getAppUser().getPseudo());
	    dto.setAppartProprioEmail(appartement.getAppUser().getEmail());
	    dto.setAppartProprioPhoneNumber(appartement.getAppUser().getPhoneNumber());

	    return dto;
	}
	
	public AppartementForProprioDTO convertToPrioprioDTO(Appartement appartement) {
		AppartementForProprioDTO dto = new AppartementForProprioDTO();
		dto.setId(appartement.getId());
		dto.setDateAchat(appartement.getDateAchat());
		dto.setNumero(appartement.getNumero());
	    dto.setAdresse(appartement.getAdresse());
	    dto.setCodePostal(appartement.getCodePostal());
	    dto.setVille(appartement.getVille());
	    dto.setNombrePieces(appartement.getNombrePieces());
	    dto.setSurface(appartement.getSurface());
	    dto.setBalcon(appartement.isBalcon());
	    dto.setPrix(appartement.getPrix());
		dto.setFraisNotaireEtNegociation(appartement.getFraisNotaireEtNegociation());
		dto.setEstimation(appartement.getEstimation());
	    dto.setDpe(appartement.getDpe());
		dto.setLastDPEUrl(appartement.getLastDPEUrl());
	    dto.setPays(appartement.getPays());
	    dto.setImages(appartement.getImages());
	    dto.setFraisFixe(appartement.getFraisFixe());
	    dto.setContacts(appartement.getContacts());
	    dto.setPeriodLocation(appartement.getPeriodLocation());
	    dto.setAppUser(convertToDTO(appartement.getAppUser()));
		List<UtilisateurDTO> appUserDTOs = appartement.getGestionnaires().stream().map(this::convertToDTO)
                .collect(Collectors.toList());
	    dto.setGestionnaires(appUserDTOs);
	    return dto;
	}
	
	public boolean estGestionnaireDeAppartement(Long userId, Appartement appartement) {
		Optional<Utilisateur> optionalUser = userRepository.findById(userId);
		if(optionalUser.isPresent()) {
		    return appartement.getGestionnaires().contains(optionalUser.get());
		}
		return false;
	}
	
	public List<Appartement> findByGestionnaireId(Long gestionnaireId){
		return appartementRepository.findByGestionnaireId(gestionnaireId);
	}

	public UtilisateurDTO convertToDTO(Utilisateur appUser) {
		UtilisateurDTO dto = new UtilisateurDTO();
		dto.setId(appUser.getId());
		dto.setPseudo(appUser.getPseudo());
	    dto.setEmail(appUser.getEmail());
	    dto.setPhoneNumber(appUser.getPhoneNumber());
	    return dto;
	}

	public List<UtilisateurDTO> obtenirGestionnairesParAppartement(Long appartId) {
        Optional<Appartement> optionalAppartement = appartementRepository.findById(appartId);
        if(!optionalAppartement.isPresent()) {
        	throw new IllegalArgumentException("Appart not found");
        }
		List<UtilisateurDTO> appUserDTOs = optionalAppartement.get().getGestionnaires().stream().map(this::convertToDTO)
                .collect(Collectors.toList());
		return appUserDTOs;
	}

	public UtilisateurDTO mettreAJourUnGestionnairePourAppartement(Long userId, Long appartementId, Long gestionnaireId, UtilisateurDTO modifieGestionnaire) {
	    Appartement appartement = appartementRepository.findById(appartementId)
	            .orElseThrow(() -> new IllegalArgumentException("Appartement not found with ID: " + appartementId));
	    
	    if (!estGestionnaireDeAppartement(gestionnaireId, appartement)) {
	        throw new SecurityException("L'utilisateur n'est pas autorisé à modifier ce gestionnaire.");
	    }
	    Optional<Utilisateur> optionalGestionnaire = userRepository.findById(gestionnaireId);
	    if(!optionalGestionnaire.isPresent()) {
        	throw new IllegalArgumentException("Gestionnaire not found");
        }
    	Utilisateur gestionnaire = optionalGestionnaire.get();
    	gestionnaire.setEmail(modifieGestionnaire.getEmail());
	    gestionnaire.setPseudo(modifieGestionnaire.getPseudo());
	    gestionnaire.setPhoneNumber(modifieGestionnaire.getPhoneNumber());
	    Utilisateur updatedGestionnaire = userRepository.save(gestionnaire);
	    return convertToDTO(updatedGestionnaire);
	    
	}


	public void supprimerUnGestionnairePourAppartement(Long userId, Long appartementId, Long gestionnaireId) {
	    Appartement appartement = appartementRepository.findById(appartementId)
	            .orElseThrow(() -> new IllegalArgumentException("Appartement not found with ID: " + appartementId));
	    
	    Optional<Utilisateur> gestionnaireOpt = userRepository.findById(gestionnaireId);
	    if (!gestionnaireOpt.isPresent()) {
	        throw new IllegalArgumentException("Gestionnaire not found with ID: " + gestionnaireId);
	    }
	    Utilisateur gestionnaire = gestionnaireOpt.get();
	    if (!estGestionnaireDeAppartement(gestionnaireId, appartement)) {
	        throw new SecurityException("L'utilisateur n'est pas autorisé à modifier ce gestionnaire.");
	    }
	    appartement.getGestionnaires().remove(gestionnaire);
	    userRepository.deleteById(gestionnaireId);
	}

	// utilisé v2 //
	public List<AdresseDTO> obtenirAdressesAppartementsParGestionnaireId(String email) {
		Optional<Utilisateur> utilisateur = userRepository.findByEmail(email);
		if(utilisateur.isPresent()) {
			return appartementRepository.obtenirAdressesAppartementsParGestionnaireId(utilisateur.get().getId());
		}
		else{
			throw new IllegalArgumentException("User not found");
		}
	}

	public List<AdresseDTO> obtenirAdressesAppartementsParUserId(String email) {
		Optional<Utilisateur> utilisateur = userRepository.findByEmail(email);
		if(utilisateur.isPresent()) {
			return appartementRepository.obtenirAdressesAppartementsParUserId(utilisateur.get().getId());
		}
		else{
			throw new IllegalArgumentException("User not found");
		}
	}

	public List<AppartementCCDTO> obtenirCCAppartementsParUserId(String email) {
		Optional<Utilisateur> utilisateur = userRepository.findByEmail(email);
		if(utilisateur.isPresent()) {
			List<AppartementCCDTO> dtos = new ArrayList<>();
			appUserService.obtenirAppartementsParUserId(utilisateur.get().getId()).forEach(a -> {
				dtos.add(appartementCalculService.creerAppartementCCDTO(a));
			});
			return dtos;
		}
		else{
			throw new IllegalArgumentException("User not found");
		}

	}

	public AppartementCCDTO obtenirCCAppartementParId(Long apartmentId) {
		Optional<Appartement> appartement = appartementRepository.findById(apartmentId);
		if(appartement.isPresent()) {
			return appartementCalculService.creerAppartementCCDTO(appartement.get());
		}
		else{
			throw new IllegalArgumentException("Appartement not found");
		}

	}

	public Appartement obtenirUnAppartementParIdAndByUtilisateurId(Long id, String email) throws IllegalArgumentException {
		Optional<Utilisateur> utilisateur = userRepository.findByEmail(email);
		if(utilisateur.isPresent()) {
			Appartement appartement = appartementRepository.findById(id)
					.orElseThrow(() -> new IllegalArgumentException("Appartement not found"));

			if (
					!estGestionnaireDeAppartement(utilisateur.get().getId(), appartement) &&
					!appartement.getAppUser().equals(utilisateur.get())) {
				throw new IllegalArgumentException("Appartement not found");
			}
			return appartement;
		}
		else{
			throw new IllegalArgumentException("User not found");
		}
	}
}