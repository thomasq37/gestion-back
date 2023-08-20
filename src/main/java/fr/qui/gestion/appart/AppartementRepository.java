package fr.qui.gestion.appart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppartementRepository extends JpaRepository<Appartement, Long> {
	
    Optional<Appartement> findById(Long id);
    
    @Override
    <S extends Appartement> S save(S entity);
    
    void deleteById(Long id);
}