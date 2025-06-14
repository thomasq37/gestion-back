package fr.qui.gestion.v2.entites.TotalCompte;

import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TotalCompteRepository extends JpaRepository<TotalCompte, Long> {
    List<TotalCompte> findByUtilisateur(Utilisateur utilisateur);
    Optional<TotalCompte> findByMasqueId(String masqueId);
}
