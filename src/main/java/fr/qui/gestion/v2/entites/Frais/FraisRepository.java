package fr.qui.gestion.v2.entites.Frais;
import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FraisRepository extends JpaRepository<Frais, Long> {
    List<Frais> findByLogement(Logement logement);
    List<Frais> findByPeriodeDeLocation(PeriodeDeLocation periodeDeLocation);
}
