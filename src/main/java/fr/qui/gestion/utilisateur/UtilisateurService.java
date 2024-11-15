package fr.qui.gestion.utilisateur;

import fr.qui.gestion.appart.Appartement;
import fr.qui.gestion.auth.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UtilisateurService(
    		UtilisateurRepository utilisateurRepository, 
    		PasswordEncoder passwordEncoder,
            InvitationRepository invitationRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Utilisateur obtenirUtilisateur(String email) {
        // Trouver l'utilisateur pour s'assurer que la tranche lui appartient
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));

        // Convertir l'Utilisateur en UtilisateurDTO
        return utilisateur;
    }

    public void modifierUtilisateur(String email, Utilisateur utilisateurModifie) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifie si l'email est modifié, s'il n'est pas pris ou s'il appartient à l'utilisateur actuel
        if (utilisateurModifie.getEmail() != null && !utilisateurModifie.getEmail().isEmpty() && !utilisateur.getEmail().equals(utilisateurModifie.getEmail())) {
            boolean emailExists = utilisateurRepository.findByEmail(utilisateurModifie.getEmail()).isPresent();
            if (emailExists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'email est déjà utilisé par un autre compte.");
            }
            utilisateur.setEmail(utilisateurModifie.getEmail());
        }

        if (utilisateurModifie.getPseudo() != null && !utilisateurModifie.getPseudo().isEmpty() && !utilisateur.getPseudo().equals(utilisateurModifie.getPseudo())) {
            boolean pseudoExists = utilisateurRepository.findByPseudo(utilisateurModifie.getPseudo()).isPresent();
            if (pseudoExists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le pseudo est déjà utilisé par un autre compte.");
            }
            utilisateur.setPseudo(utilisateurModifie.getPseudo());
        }

        // Vérifie si le mot de passe fait au moins dix caractères
        if (utilisateurModifie.getMdp() != null && !utilisateurModifie.getMdp().isEmpty()) {
            if (utilisateurModifie.getMdp().length() < 10) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le mot de passe doit contenir au moins 10 caractères.");
            }
            utilisateur.setMdp(passwordEncoder.encode(utilisateurModifie.getMdp()));
        }
        utilisateurRepository.save(utilisateur);

    }

    public void supprimerCompte(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));

        utilisateurRepository.delete(utilisateur);
    }

    public void supprimerUtilisateurParAdmin(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));

        utilisateurRepository.delete(utilisateur);
    }

    public List<Appartement> obtenirAppartementsParUserId(Long userId) {
        Optional<Utilisateur> optionalUser = utilisateurRepository.findById(userId);
        if(optionalUser.isPresent()) {
            return optionalUser.get().getAppartements();
        }
        else {
            throw new IllegalArgumentException("Appartements not found for the provided user.");
        }
    }
}
