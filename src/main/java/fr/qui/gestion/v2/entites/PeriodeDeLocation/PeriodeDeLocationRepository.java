package fr.qui.gestion.v2.entites.PeriodeDeLocation;
import fr.qui.gestion.v2.entites.Logement.Logement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeriodeDeLocationRepository extends JpaRepository<PeriodeDeLocation, Long> {
    List<PeriodeDeLocation> findByLogement(Logement logement);
}