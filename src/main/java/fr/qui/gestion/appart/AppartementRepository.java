package fr.qui.gestion.appart;

import java.util.List;
import java.util.Optional;

import fr.qui.gestion.appart.dto.ChiffresClesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.qui.gestion.appart.dto.AdresseDTO;

@Repository
public interface AppartementRepository extends JpaRepository<Appartement, Long> {
	
    Optional<Appartement> findById(Long id);
    void deleteById(Long id);

    @Query("SELECT a FROM Appartement a JOIN a.gestionnaires g WHERE g.id = :gestionnaireId")
    List<Appartement> findByGestionnaireId(Long gestionnaireId);

    // utilisé v2 //
    @Query("select new fr.qui.gestion.appart.dto.AdresseDTO(a.id, a.numero, a.adresse, a.codePostal, a.ville, a.pays) from Appartement a where a.appUser.id = :userId")
    List<AdresseDTO> obtenirAdressesAppartementsParUserId(@Param("userId") Long userId);
    @Query("SELECT new fr.qui.gestion.appart.dto.AdresseDTO(a.id, a.numero, a.adresse, a.codePostal, a.ville, a.pays) from Appartement a JOIN a.gestionnaires g WHERE g.id = :gestionnaireId")
    List<AdresseDTO> obtenirAdressesAppartementsParGestionnaireId(Long gestionnaireId);

    List<ChiffresClesDTO> obtenirPrixTotalAppartementsParUserId(Long id);
}