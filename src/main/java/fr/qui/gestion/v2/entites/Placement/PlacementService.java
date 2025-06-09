package fr.qui.gestion.v2.entites.Placement;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.entites.Utilisateur.UtilisateurRepository;
import fr.qui.gestion.v2.exception.SuccessResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlacementService {

    private final PlacementRepository placementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PlacementMapper placementMapper;

    public PlacementService(
            PlacementRepository placementRepository,
            UtilisateurRepository utilisateurRepository,
            PlacementMapper placementMapper) {
        this.placementRepository = placementRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.placementMapper = placementMapper;
    }

    public PlacementVueEnsembleDTO creerPlacement(PlacementVueEnsembleDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Accès interdit ou utilisateur introuvable."));

        Placement placement = new Placement();
        placement.setNom(dto.getNom());
        placement.setCapital(dto.getCapital());
        placement.setUtilisateur(utilisateur);

        placementRepository.save(placement);
        return placementMapper.toVueEnsembleDTO(placement);
    }

    public List<PlacementVueEnsembleDTO> listerPlacements() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Accès interdit ou utilisateur introuvable."));

        List<Placement> placements = placementRepository.findByUtilisateur(utilisateur);
        return placements.stream().map(placementMapper::toVueEnsembleDTO).toList();
    }

    public PlacementVueEnsembleDTO obtenirPlacement(String masqueId) {
        Placement placement = placementRepository.findByMasqueId(masqueId)
                .orElseThrow(() -> new SecurityException("Accès interdit ou placement introuvable."));
        return placementMapper.toVueEnsembleDTO(placement);
    }

    public PlacementVueEnsembleDTO modifierPlacement(String masqueId, PlacementVueEnsembleDTO dto) {
        Placement placement = placementRepository.findByMasqueId(masqueId)
                .orElseThrow(() -> new SecurityException("Accès interdit ou placement introuvable."));

        placement.setNom(dto.getNom());
        placement.setCapital(dto.getCapital());

        return placementMapper.toVueEnsembleDTO(placementRepository.save(placement));
    }

    public SuccessResponse supprimerPlacement(String placementMasqueId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Acces interdit ou utilisateur introuvable."));
        Placement placement = placementRepository.findByMasqueId(placementMasqueId)
                .orElseThrow(() -> new SecurityException("Acces interdit ou placemeent introuvable."));
        if (!placement.getUtilisateur().equals(utilisateur)) {
            throw new SecurityException("Acces interdit ou placement introuvable.");
        }
        placementRepository.delete(placement);
        return new SuccessResponse("Le placement a été supprimé avec succès.");
    }
}
