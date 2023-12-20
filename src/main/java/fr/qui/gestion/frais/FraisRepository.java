package fr.qui.gestion.frais;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface FraisRepository extends JpaRepository<Frais, Long> {
	
    Page<Frais> findByAppartementId(Long appartId, Pageable pageable);
    
    Page<Frais> findByPeriodLocationId(Long periodeId, Pageable pageable);
    
    Optional<Frais> findById(Long id);
    
    @Override
    <S extends Frais> S save(S entity);
    
    void deleteById(Long id);
    
    void delete(Frais frais);
    
    void deleteAllByAppartementId(Long appartementId);
    
    @Modifying
    @Transactional
    void deleteByPeriodLocationId(Long periodLocationId);    
}