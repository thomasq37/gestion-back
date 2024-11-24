package fr.qui.gestion.v2.entites.Utilisateur;
import fr.qui.gestion.v2.auth.RegisterUserRequestDTO;
import fr.qui.gestion.v2.exception.SuccessResponse;
import fr.qui.gestion.v2.security.CustomUtilisateurDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final PasswordEncoder passwordEncoder;
    private final CustomUtilisateurDetailsService customUtilisateurDetailsService;

    @Autowired
    public UtilisateurService(
    		UtilisateurRepository utilisateurRepository,
            UtilisateurMapper utilisateurMapper,
    		PasswordEncoder passwordEncoder,
            CustomUtilisateurDetailsService customUtilisateurDetailsService) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
        this.passwordEncoder = passwordEncoder;
        this.customUtilisateurDetailsService = customUtilisateurDetailsService;
    }

    public UtilisateurDTO obtenirUtilisateur() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(email);
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Acces interdit ou utilisateur introuvable."));
        return utilisateurMapper.toDto(utilisateur);
    }
    public UtilisateurDTO modifierUtilisateur(UtilisateurUpdateDTO utilisateurModifie) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Acces interdit ou utilisateur introuvable."));
        if (utilisateurModifie.getEmail() != null && !utilisateurModifie.getEmail().isEmpty()) {
            if (!utilisateur.getEmail().equals(utilisateurModifie.getEmail())) {
                boolean emailExists = utilisateurRepository.findByEmail(utilisateurModifie.getEmail()).isPresent();
                if (emailExists) {
                    throw new IllegalArgumentException("L'email est déjà utilisé par un autre compte.");
                }
                utilisateur.setEmail(utilisateurModifie.getEmail());
            }
        }
        if (utilisateurModifie.getMdp() != null && !utilisateurModifie.getMdp().isEmpty()) {
            String mdp = utilisateurModifie.getMdp();
            if (mdp.length() < 8) {
                throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères.");
            }
            if (!mdp.matches(".*\\d.*")) {
                throw new IllegalArgumentException("Le mot de passe doit contenir au moins un chiffre.");
            }
            if (!mdp.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                throw new IllegalArgumentException("Le mot de passe doit contenir au moins un caractère spécial.");
            }
            utilisateur.setMdp(passwordEncoder.encode(mdp));
        }
        if (utilisateurModifie.getTelephone() == null || utilisateurModifie.getTelephone().isEmpty()){
            utilisateur.setTelephone(null);
        }
        else {
            String telephone = utilisateurModifie.getTelephone();
            if (!telephone.matches("^(\\+\\d{1,3}[- ]?)?\\d{10}$")) {
                throw new IllegalArgumentException("Le numéro de téléphone est invalide.");
            }
            utilisateur.setTelephone(utilisateurModifie.getTelephone());
        }
        utilisateur.setNom(utilisateurModifie.getNom());
        utilisateur.setPrenom(utilisateurModifie.getPrenom());
        utilisateur = utilisateurRepository.save(utilisateur);
        mettreAJourSecurityContext(utilisateur);
        return utilisateurMapper.toDto(utilisateur);
    }

    public SuccessResponse supprimerCompte() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Acces interdit ou utilisateur introuvable."));
        utilisateurRepository.delete(utilisateur);
        return new SuccessResponse("Le compte a été supprimé avec succès.");
    }

    public SuccessResponse supprimerUtilisateur(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Acces interdit ou utilisateur introuvable."));
        utilisateurRepository.delete(utilisateur);
        return new SuccessResponse("Le compte a été supprimé avec succès.");
    }

    private void mettreAJourSecurityContext(Utilisateur utilisateur) {
        UserDetails userDetails = customUtilisateurDetailsService.loadUserByUsername(utilisateur.getEmail());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
