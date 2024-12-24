package fr.qui.gestion.v2.entites.Photo;
import fr.qui.gestion.v2.entites.Logement.Logement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByLogement(Logement logement);
    Photo findFirstByLogementAndIsPrincipalTrue(Logement logement);
}
