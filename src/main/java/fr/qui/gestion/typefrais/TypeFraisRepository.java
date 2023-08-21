package fr.qui.gestion.typefrais;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeFraisRepository extends JpaRepository<TypeFrais, Long> {
	
   Optional<TypeFrais> findById(Long id);
	   
    @Override
    <S extends TypeFrais> S save(S entity);
    
    void deleteById(Long id);

}