package fr.qui.gestion.frais;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraisRepository extends JpaRepository<Frais, Long> {
	
    List<Frais> findByAppartementId(Long appartementId);
    
    Optional<Frais> findById(Long id);
    
    @Override
    <S extends Frais> S save(S entity);
    
    void deleteById(Long id);
    
    void deleteAllByAppartementId(Long appartementId);
    
}