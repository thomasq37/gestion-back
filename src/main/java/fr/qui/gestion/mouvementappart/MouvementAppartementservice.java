package fr.qui.gestion.mouvementappart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
}