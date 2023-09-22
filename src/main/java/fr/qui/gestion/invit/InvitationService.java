package fr.qui.gestion.invit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationService {
    @Autowired
    private InvitationRepository invitationRepository;


    public boolean validateInvitation(String token) {
        Invitation invitation = invitationRepository.findByToken(token);
        if(invitation != null && !invitation.isUsed()) {
        	invitation.setUsed(true);
            invitationRepository.save(invitation);
            return true;
        }
        return false;
    }
}
