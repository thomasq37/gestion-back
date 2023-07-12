package fr.qui.gestion.mouvementappart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MouvementAppartementRepository extends JpaRepository<MouvementAppartement, Long> {
    List<MouvementAppartement> findByAppartementIdAndDateBetween(Long appartementId, LocalDate dateDebut, LocalDate dateFin);
}