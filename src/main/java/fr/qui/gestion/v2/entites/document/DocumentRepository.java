package fr.qui.gestion.v2.entites.document;

import fr.qui.gestion.v2.entites.Logement.Logement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByLogementsContaining(Logement logement);
    long countByLogementsContaining(Document document);

    Optional<Document> findByMasqueId(String masqueId);
}
