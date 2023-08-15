package fr.qui.gestion.frais;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraisRepository extends JpaRepository<Frais, Long> {
    List<Frais> findByAppartementId(Long appartementId);
}