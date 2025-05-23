package fr.qui.gestion.v2.entites.Logement;

import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogementRepository extends JpaRepository<Logement, Long> {
    @Query("""
           SELECT l
           FROM Logement l
           LEFT JOIN l.caracteristiques c
           WHERE l.proprietaire = :proprietaire
           ORDER BY CASE WHEN c IS NULL THEN 1 ELSE 0 END, c.dateAchat DESC
           """)
    List<Logement> findByProprietaire(Utilisateur proprietaire);

    @Query("""
    SELECT l FROM Logement l 
    LEFT JOIN FETCH l.photos p 
    WHERE l.proprietaire = :proprietaire 
    AND (p.isPrincipal = true OR p IS NULL) 
    ORDER BY 
        CASE WHEN p IS NULL THEN 1 ELSE 0 END, 
        l.id DESC
    """)
    List<Logement> findByProprietaireWithPhotoPrincipale(@Param("proprietaire") Utilisateur proprietaire);
    Optional<Logement> findByMasqueId(String masqueId);
}
