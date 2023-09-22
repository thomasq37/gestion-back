package fr.qui.gestion.invit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invitation")
public class InvitationController {
    @Autowired
    private InvitationService invitationService;
    

    @GetMapping("/validate/{token}")
    public ResponseEntity<String> validateInvitation(@PathVariable String token) {
        if(invitationService.validateInvitation(token))
            return ResponseEntity.ok("Invitation is valid and marked as used");
        else
            return ResponseEntity.status(400).body("Invalid or already used invitation");
    }
}
