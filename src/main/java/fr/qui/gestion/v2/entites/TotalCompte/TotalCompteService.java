package fr.qui.gestion.v2.entites.TotalCompte;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.entites.Utilisateur.UtilisateurRepository;
import fr.qui.gestion.v2.exception.SuccessResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TotalCompteService {

    private final TotalCompteRepository totalCompteRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final TotalCompteMapper totalCompteMapper;

    public TotalCompteService(
            TotalCompteRepository totalCompteRepository,
            UtilisateurRepository utilisateurRepository,
            TotalCompteMapper totalCompteMapper) {
        this.totalCompteRepository = totalCompteRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.totalCompteMapper = totalCompteMapper;
    }

    public TotalCompteDTO creerTotalCompte(TotalCompteDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Accès interdit ou utilisateur introuvable."));

        TotalCompte totalCompte = new TotalCompte();
        totalCompte.setMontant(dto.getMontant());
        totalCompte.setDateEnregistrement(dto.getDateEnregistrement());
        totalCompte.setUtilisateur(utilisateur);

        totalCompteRepository.save(totalCompte);
        return totalCompteMapper.toVueEnsembleDTO(totalCompte);
    }

    public List<TotalCompteDTO> listerTotalComptes() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Accès interdit ou utilisateur introuvable."));

        List<TotalCompte> totalComptes = totalCompteRepository.findByUtilisateur(utilisateur);
        return totalComptes.stream().map(totalCompteMapper::toVueEnsembleDTO).toList();
    }

    public TotalCompteDTO obtenirTotalCompte(String masqueId) {
        TotalCompte totalCompte = totalCompteRepository.findByMasqueId(masqueId)
                .orElseThrow(() -> new SecurityException("Accès interdit ou totalCompte introuvable."));
        return totalCompteMapper.toVueEnsembleDTO(totalCompte);
    }

    public TotalCompteDTO modifierTotalCompte(String masqueId, TotalCompteDTO dto) {
        TotalCompte totalCompte = totalCompteRepository.findByMasqueId(masqueId)
                .orElseThrow(() -> new SecurityException("Accès interdit ou totalCompte introuvable."));

        totalCompte.setMontant(dto.getMontant());
        totalCompte.setDateEnregistrement(dto.getDateEnregistrement());

        return totalCompteMapper.toVueEnsembleDTO(totalCompteRepository.save(totalCompte));
    }

    public SuccessResponse supprimerTotalCompte(String totalCompteMasqueId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Acces interdit ou utilisateur introuvable."));
        TotalCompte totalCompte = totalCompteRepository.findByMasqueId(totalCompteMasqueId)
                .orElseThrow(() -> new SecurityException("Acces interdit ou placemeent introuvable."));
        if (!totalCompte.getUtilisateur().equals(utilisateur)) {
            throw new SecurityException("Acces interdit ou totalCompte introuvable.");
        }
        totalCompteRepository.delete(totalCompte);
        return new SuccessResponse("Le totalCompte a été supprimé avec succès.");
    }
}
