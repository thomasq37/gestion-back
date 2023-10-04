package fr.qui.gestion.appart;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppartementRepository extends JpaRepository<Appartement, Long> {
	
    Optional<Appartement> findById(Long id);
    
    @Query("select new fr.qui.gestion.appart.AdresseDTO(a.id, a.numero, a.adresse, a.codePostal, a.ville) from Appartement a")
    List<AdresseDTO> findAllAdresses();
    
    @Query("select new fr.qui.gestion.appart.AdresseDTO(a.id, a.numero, a.adresse, a.codePostal, a.ville) from Appartement a where a.appUser.id = :userId")
    List<AdresseDTO> findAdressesByUserId(@Param("userId") Long userId);

    
    @Override
    <S extends Appartement> S save(S entity);
    
    void deleteById(Long id);
}