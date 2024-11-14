package fr.qui.gestion.security;

import fr.qui.gestion.utilisateur.Utilisateur;
import fr.qui.gestion.utilisateur.UtilisateurRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUtilisateurDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private static final String DEFAULT_ROLE = "ROLE_USER";

    public CustomUtilisateurDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nom) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(nom)
            .orElseThrow(() -> new UsernameNotFoundException("L'utilisateur n'a pas été trouvé : " + nom));

        // Créer une liste de GrantedAuthority à partir des rôles de l'utilisateur
        List<GrantedAuthority> authorities;
        if (utilisateur.getRoles().isEmpty()) {
            // Attribuer le rôle par défaut si aucun rôle n'est trouvé
            authorities = Collections.singletonList(new SimpleGrantedAuthority(DEFAULT_ROLE));
        } else {
            // Sinon, utiliser les rôles existants
            authorities = utilisateur.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getNom()))
                .collect(Collectors.toList());
        }

        return new CustomUtilisateurDetails(
            utilisateur.getEmail(),
            utilisateur.getMdp(),
            authorities,
            utilisateur.getId());
    }
}
