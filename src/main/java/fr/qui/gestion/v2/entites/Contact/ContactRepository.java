package fr.qui.gestion.v2.entites.Contact;
import fr.qui.gestion.v2.entites.Logement.Logement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByLogement(Logement logement);
}
