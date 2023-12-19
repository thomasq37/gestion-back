package fr.qui.gestion.appart;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.qui.gestion.appart.dto.AdresseDTO;

@Repository
public interface AppartementRepository extends JpaRepository<Appartement, Long> {
	
    Optional<Appartement> findById(Long id);
    
    @Query("SELECT a FROM Appartement a JOIN a.gestionnaires g WHERE g.id = :gestionnaireId")
    List<Appartement> findByGestionnaireId(Long gestionnaireId);
    
    @Query("select new fr.qui.gestion.appart.dto.AdresseDTO(a.id, a.numero, a.adresse, a.codePostal, a.ville) from Appartement a")
    List<AdresseDTO> findAllAdresses();
    
    @Query("select new fr.qui.gestion.appart.dto.AdresseDTO(a.id, a.numero, a.adresse, a.codePostal, a.ville) from Appartement a where a.appUser.id = :userId")
    List<AdresseDTO> findAdressesByUserId(@Param("userId") Long userId);

    
    @Query("select new fr.qui.gestion.appart.dto.AdresseDTO(a.id, a.numero, a.adresse, a.codePostal, a.ville) from Appartement a where a.appUser.userToken = :userToken")
    List<AdresseDTO> findAdressesByUserToken(@Param("userToken") String userToken);
    
    void deleteById(Long id);
}