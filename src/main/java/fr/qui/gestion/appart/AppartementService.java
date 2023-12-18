package fr.qui.gestion.appart;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.qui.gestion.frais.Frais;
import fr.qui.gestion.frais.FraisRepository;
import fr.qui.gestion.periodlocation.PeriodLocation;
import fr.qui.gestion.periodlocation.PeriodLocationRepository;
import fr.qui.gestion.user.AppUser;
import fr.qui.gestion.user.AppUserDTO;
import jakarta.transaction.Transactional;

@Service
public class AppartementService {
    

    
    @Autowired
    private FraisRepository fraisRepository;
    
    @Autowired
    private PeriodLocationRepository periodLocationRepository;
    
    @Autowired
    private AppartementRepository appartementRepository;
    
    public List<Appartement> obtenirTousLesAppartements() {
        return appartementRepository.findAll();
    }
    
    public Appartement ajouterAppartement(Appartement nouvelAppartement) {
        return appartementRepository.save(nouvelAppartement);
    }
    
    public Appartement mettreAJourUnAppartementPourUtilisateur(Long userId, Long appartId, Appartement appartementModifie) {
        Appartement appartementExist = obtenirUnAppartementParId(appartId);
        if (appartementExist.getAppUser().getId() != userId) {
            throw new SecurityException("L'utilisateur n'est pas autorisé à mettre à jour cet appartement.");
        }
        appartementExist.setNumero(appartementModifie.getNumero());
        appartementExist.setAdresse(appartementModifie.getAdresse());
        appartementExist.setCodePostal(appartementModifie.getCodePostal());
        appartementExist.setVille(appartementModifie.getVille());
        appartementExist.setNombrePieces(appartementModifie.getNombrePieces());
        appartementExist.setSurface(appartementModifie.getSurface());
        appartementExist.setBalcon(appartementModifie.isBalcon());
        appartementExist.setPrix(appartementModifie.getPrix());
        appartementExist.setImages(appartementModifie.getImages());
        return appartementRepository.save(appartementExist);
    }

    public Appartement obtenirUnAppartementParId(Long id) throws IllegalArgumentException {
        Optional<Appartement> optionalAppartement = appartementRepository.findById(id);
        return optionalAppartement.orElseThrow(() -> new IllegalArgumentException("Appartement not found with ID: " + id));
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
	    dto.setImages(appartement.getImages());
	    dto.setPeriodLocation(appartement.getPeriodLocation());
	    dto.setAppartProprioUsername(appartement.getAppUser().getUsername());
	    dto.setAppartProprioEmail(appartement.getAppUser().getEmail());
	    dto.setAppartProprioPhoneNumber(appartement.getAppUser().getPhoneNumber());

	    return dto;
	}
	
	public AppartementForProprioDTO convertToPrioprioDTO(Appartement appartement) {
		AppartementForProprioDTO dto = new AppartementForProprioDTO();
		dto.setId(appartement.getId());
		dto.setNumero(appartement.getNumero());
	    dto.setAdresse(appartement.getAdresse());
	    dto.setCodePostal(appartement.getCodePostal());
	    dto.setVille(appartement.getVille());
	    dto.setNombrePieces(appartement.getNombrePieces());
	    dto.setSurface(appartement.getSurface());
	    dto.setBalcon(appartement.isBalcon());
	    dto.setImages(appartement.getImages());
	    dto.setPeriodLocation(appartement.getPeriodLocation());
	    dto.setAppUser(convertToDTO(appartement.getAppUser()));
		List<AppUserDTO> appUserDTOs = appartement.getGestionnaires().stream().map(this::convertToDTO)
                .collect(Collectors.toList());
	    dto.setGestionnaires(appUserDTOs);
	    return dto;
	}
	
	public boolean estGestionnaireDeAppartement(AppUser user, Appartement appartement) {
	    return appartement.getGestionnaires().contains(user);
	}

	public AppUserDTO convertToDTO(AppUser appUser) {
		AppUserDTO dto = new AppUserDTO();
		dto.setId(appUser.getId());
		dto.setUsername(appUser.getUsername());
	    dto.setEmail(appUser.getEmail());
	    dto.setPhoneNumber(appUser.getPhoneNumber());
	    return dto;
	}

	public List<AppUserDTO> obtenirGestionnairesParAppartement(Long appartId) {
        Optional<Appartement> optionalAppartement = appartementRepository.findById(appartId);
        if(!optionalAppartement.isPresent()) {
        	throw new IllegalArgumentException("Appart not found");
        }
		List<AppUserDTO> appUserDTOs = optionalAppartement.get().getGestionnaires().stream().map(this::convertToDTO)
                .collect(Collectors.toList());
		return appUserDTOs;
	}

	

}