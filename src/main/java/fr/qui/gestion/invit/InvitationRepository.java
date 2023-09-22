package fr.qui.gestion.invit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Invitation findByToken(String token);
}
