package fr.qui.gestion.v2.entites.Photo;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.entites.Logement.LogementRepository;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.entites.Utilisateur.UtilisateurRepository;
import fr.qui.gestion.v2.exception.SuccessResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final LogementRepository logementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PhotoMapper photoMapper;

    public PhotoService(
            PhotoRepository photoRepository,
            LogementRepository logementRepository,
            UtilisateurRepository utilisateurRepository,
            PhotoMapper photoMapper) {
        this.photoRepository = photoRepository;
        this.logementRepository = logementRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.photoMapper = photoMapper;
    }
    public List<PhotoDTO> listerPhotos(String logementMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Acces interdit ou utilisateur introuvable."));
        List<Photo> photos = photoRepository.findByLogement(logement);
        return photos.stream().map(photoMapper::toDto).toList();
    }
    @Transactional
    public PhotoDTO creerPhotoPourLogement(String logementMasqueId, PhotoDTO photoDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);

        if (photoDTO.getImage() == null) {
            throw new IllegalArgumentException("L'image est obligatoire.");
        }

        if (Boolean.TRUE.equals(photoDTO.getIsPrincipal())) {
            // Vérifier s'il existe déjà une photo principale
            Photo photoPrincipaleExistante = photoRepository.findFirstByLogementAndIsPrincipalTrue(logement);
            if (photoPrincipaleExistante != null) {
                // Définir l'ancienne photo principale comme non principale
                photoPrincipaleExistante.setIsPrincipal(false);
                photoRepository.save(photoPrincipaleExistante);
            }
        }

        Photo photo = new Photo();
        photo.setImage(photoDTO.getImage());
        photo.setIsPrincipal(photoDTO.getIsPrincipal());
        photo.setLogement(logement);

        Photo savedPhoto = photoRepository.save(photo);
        logement.getPhotos().add(savedPhoto);
        logementRepository.save(logement);

        return photoMapper.toDto(savedPhoto);
    }

    @Transactional(readOnly = true)
    public PhotoDTO obtenirPhotoPourLogement(String logementMasqueId, String photoMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Photo photo = logement.getPhotos().stream()
                .filter(c -> c.getMasqueId().equals(photoMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou photo introuvable."));
        return photoMapper.toDto(photo);
    }
    public PhotoDTO obtenirPhotoPrincipalePourLogement(String logementMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Photo photoPrincipale = photoRepository.findFirstByLogementAndIsPrincipalTrue(logement);
        if (photoPrincipale == null) {
            throw new IllegalArgumentException("Aucune photo principale trouvée pour ce logement.");
        }
        return photoMapper.toDto(photoPrincipale);
    }
    @Transactional
    public PhotoDTO modifierPhotoPourLogement(String logementMasqueId, String photoMasqueId, PhotoDTO photoModifieeDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);

        Photo photo = logement.getPhotos().stream()
                .filter(c -> c.getMasqueId().equals(photoMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Accès interdit ou photo introuvable."));

        if (photoModifieeDTO.getImage() == null) {
            throw new IllegalArgumentException("L'image est obligatoire.");
        }

        if (Boolean.TRUE.equals(photoModifieeDTO.getIsPrincipal()) && !Boolean.TRUE.equals(photo.getIsPrincipal())) {
            // Vérifier s'il existe déjà une photo principale
            Photo photoPrincipaleExistante = photoRepository.findFirstByLogementAndIsPrincipalTrue(logement);
            if (photoPrincipaleExistante != null) {
                // Définir l'ancienne photo principale comme non principale
                photoPrincipaleExistante.setIsPrincipal(false);
                photoRepository.save(photoPrincipaleExistante);
            }
        }

        photo.setImage(photoModifieeDTO.getImage());
        photo.setIsPrincipal(photoModifieeDTO.getIsPrincipal());

        Photo savedPhoto = photoRepository.save(photo);
        return photoMapper.toDto(savedPhoto);
    }

    @Transactional
    public SuccessResponse supprimerPhotoPourLogement(String logementMasqueId, String photoMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Photo photo = logement.getPhotos().stream()
                .filter(c -> c.getMasqueId().equals(photoMasqueId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Acces interdit ou photo introuvable."));
        logement.getPhotos().remove(photo);
        logementRepository.save(logement);
        photoRepository.delete(photo);
        return new SuccessResponse("Le photo a été supprimée avec succès.");

    }
    private Logement validerLogementPourUtilisateur(String logementMasqueId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Accès interdit ou utilisateur introuvable."));
        Logement logement = logementRepository.findByMasqueId(logementMasqueId)
                .orElseThrow(() -> new SecurityException("Accès interdit ou logement introuvable."));
        if (!logement.getProprietaire().equals(utilisateur)) {
            throw new SecurityException("Accès interdit ou logement introuvable.");
        }
        return logement;
    }
}
