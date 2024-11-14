package fr.qui.gestion.utilisateur;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long>{
	// étends des méhodes de JpaRepository
	Optional<Utilisateur> findByEmail(String nom);
    Optional<Utilisateur> findByPseudo(String pseudo);

    boolean existsByEmail(String nom);
    Page<Utilisateur> findByIdIn(List<Long> ids, Pageable pageable);

    boolean existsByPseudo(String pseudo);
}
