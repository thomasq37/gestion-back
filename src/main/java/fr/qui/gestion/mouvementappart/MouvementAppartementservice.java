package fr.qui.gestion.mouvementappart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.frais.Frais;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MouvementAppartementservice {
    private MouvementAppartementRepository mouvementAppartementRepository;

    @Autowired
    public MouvementAppartementservice(MouvementAppartementRepository mouvementAppartementRepository) {
        this.mouvementAppartementRepository = mouvementAppartementRepository;
    }

    public List<MouvementAppartement> findByAppartementIdAndDateBetween(Long appartementId, LocalDate dateDebut, LocalDate dateFin) {
        return mouvementAppartementRepository.findByAppartementIdAndDateBetween(appartementId, dateDebut, dateFin);
    }

	public List<MouvementAppartement> obtenirTousLesMouvAppartements() {
		return mouvementAppartementRepository.findAll();
	}
	
	public MouvementAppartement obtenirUnMouvAppartementParId(Long id) throws IllegalArgumentException {
        Optional<MouvementAppartement> optionalFrais = mouvementAppartementRepository.findById(id);
        return optionalFrais.orElseThrow(() -> new IllegalArgumentException("MouvementAppartement not found with ID: " + id));
    }

	public MouvementAppartement modifierMouvementAppartement(Long id, MouvementAppartement mouvementAppartementModifie) {
		MouvementAppartement mouvementAppartementExist = obtenirUnMouvAppartementParId(id);

        if (mouvementAppartementExist == null) {
            throw new IllegalArgumentException("MouvementAppartement with ID " + id + " not found");
        }

        mouvementAppartementExist.setDate(mouvementAppartementModifie.getDate());
        mouvementAppartementExist.setEstEntree(mouvementAppartementModifie.isEstEntree());

        return mouvementAppartementRepository.save(mouvementAppartementExist);
	}

	public void supprimerUnMouvement(Long id) {
		mouvementAppartementRepository.deleteById(id);
		
	}

	public MouvementAppartement ajouterMouvementAppartement(MouvementAppartement nouveauMouvementAppartement) {
		return mouvementAppartementRepository.save(nouveauMouvementAppartement);
	}
}