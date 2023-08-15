package fr.qui.gestion.typefrais;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeFraisRepository extends JpaRepository<TypeFrais, Long> {
}