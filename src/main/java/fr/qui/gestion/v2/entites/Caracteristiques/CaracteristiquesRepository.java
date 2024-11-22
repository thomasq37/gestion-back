package fr.qui.gestion.v2.entites.Caracteristiques;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaracteristiquesRepository extends JpaRepository<Caracteristiques, Long> {
}
