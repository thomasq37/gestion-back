package fr.qui.gestion.periodlocation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.qui.gestion.frais.Frais;

@Repository
public interface PeriodLocationRepository extends JpaRepository<PeriodLocation, Long> {
	List<PeriodLocation> findByAppartementId(Long appartId);
}