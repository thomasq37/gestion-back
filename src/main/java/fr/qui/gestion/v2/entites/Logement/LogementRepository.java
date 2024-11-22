package fr.qui.gestion.v2.entites.Logement;

import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogementRepository extends JpaRepository<Logement, Long> {
    List<Logement> findByProprietaire(Utilisateur proprietaire);

    Optional<Logement> findByMasqueId(String masqueId);
}
