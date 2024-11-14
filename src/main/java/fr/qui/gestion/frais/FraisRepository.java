package fr.qui.gestion.frais;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface FraisRepository extends JpaRepository<Frais, Long> {

    @Query("SELECT f FROM Frais f WHERE f.appartement.id = :appartId " +
            "ORDER BY CASE WHEN f.frequence = 'PONCTUELLE' THEN 1 ELSE 0 END, " +
            "COALESCE(f.datePaiement, '1900-01-01') DESC, " +
            "f.frequence")
    Page<Frais> findByAppartementIdOrderByDatePaiementDesc(@Param("appartId") Long appartId, Pageable pageable);

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