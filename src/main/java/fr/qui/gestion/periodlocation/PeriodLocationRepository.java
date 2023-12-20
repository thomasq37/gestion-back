package fr.qui.gestion.periodlocation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodLocationRepository extends JpaRepository<PeriodLocation, Long> {
	Page<PeriodLocation> findByAppartementId(Long appartId, Pageable pageable);
}