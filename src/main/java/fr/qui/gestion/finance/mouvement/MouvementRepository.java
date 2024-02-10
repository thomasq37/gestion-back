package fr.qui.gestion.finance.mouvement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MouvementRepository extends JpaRepository<Mouvement, Long> {
    // Vous pouvez ajouter des méthodes personnalisées ici si nécessaire
}
