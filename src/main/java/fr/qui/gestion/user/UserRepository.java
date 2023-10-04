package fr.qui.gestion.user;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.qui.gestion.appart.Appartement;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    
    Optional<AppUser> findByUserToken(String token);
    
    @Query("SELECT a FROM Appartement a WHERE a.appUser.id = :userId AND a.id = :apartmentId")
    Optional<Appartement> findAppartementByAppUserIdAndId(@Param("userId") Long userId, @Param("apartmentId") Long apartmentId);
}
