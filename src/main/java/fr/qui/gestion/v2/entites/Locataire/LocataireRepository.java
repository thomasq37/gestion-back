package fr.qui.gestion.v2.entites.Locataire;
import fr.qui.gestion.v2.entites.PeriodeDeLocation.PeriodeDeLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocataireRepository extends JpaRepository<Locataire, Long> {
    List<Locataire> findByPeriodeDeLocation(PeriodeDeLocation periodeDeLocation);
}