package fr.qui.gestion.periodlocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.qui.gestion.frais.Frais;

@Repository
public interface PeriodLocationRepository extends JpaRepository<PeriodLocation, Long> {
	void delete(PeriodLocation periodLocation);
	
    @Override
    <S extends PeriodLocation> S save(S entity);
    //List<PeriodLocation> findByAppartementIdAndDateBetween(Long appartementId, LocalDate dateDebut, LocalDate dateFin);
}