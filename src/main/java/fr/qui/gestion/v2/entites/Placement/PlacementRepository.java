package fr.qui.gestion.v2.entites.Placement;

import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlacementRepository extends JpaRepository<Placement, Long> {
    List<Placement> findByUtilisateur(Utilisateur utilisateur);
    Optional<Placement> findByMasqueId(String masqueId);
}
