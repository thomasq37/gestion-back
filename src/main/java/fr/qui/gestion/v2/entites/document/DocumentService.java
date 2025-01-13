package fr.qui.gestion.v2.entites.document;

import fr.qui.gestion.v2.entites.Logement.Logement;
import fr.qui.gestion.v2.entites.Logement.LogementRepository;
import fr.qui.gestion.v2.entites.Utilisateur.Utilisateur;
import fr.qui.gestion.v2.entites.Utilisateur.UtilisateurRepository;
import fr.qui.gestion.v2.exception.SuccessResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final LogementRepository logementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final DocumentMapper documentMapper;

    public DocumentService(
            DocumentRepository documentRepository,
            LogementRepository logementRepository,
            UtilisateurRepository utilisateurRepository,
            DocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.logementRepository = logementRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.documentMapper = documentMapper;
    }

    public List<DocumentDTO> listerDocuments(String logementMasqueId) {
        // Valider que le logement appartient à l'utilisateur
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);

        // Récupérer les documents associés au logement
        List<Document> documents = documentRepository.findByLogementsContaining(logement);

        // Convertir les documents en DTO
        return documents.stream()
                .map(documentMapper::toDto)
                .toList();
    }


    @Transactional
    public DocumentDTO ajouterDocument(String logementMasqueId, DocumentDTO documentDTO) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);

        if (documentDTO.getNom() == null || documentDTO.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le champ nom est obligatoire.");
        }
        if (documentDTO.getFichier() == null || documentDTO.getFichier().isEmpty()) {
            throw new IllegalArgumentException("Le champ fichier est obligatoire.");
        }

        // Création et sauvegarde du document
        Document document = new Document();
        document.setNom(documentDTO.getNom());
        document.setFichier(documentDTO.getFichier());
        // S'assurer que la liste logements n'est pas null
        if (document.getLogements() == null) {
            document.setLogements(new ArrayList<>());
        }
        document.getLogements().add(logement); // Ajout du logement au document
        Document savedDocument = documentRepository.save(document);

        // Ajout du document au logement
        logement.getDocuments().add(savedDocument);
        logementRepository.save(logement);

        // Conversion en DTO et retour
        return documentMapper.toDto(savedDocument);
    }


    @Transactional(readOnly = true)
    public DocumentDTO obtenirDocument(String documentMasqueId) {
        Document document = documentRepository.findById(Long.valueOf(documentMasqueId))
                .orElseThrow(() -> new SecurityException("Document introuvable."));
        return documentMapper.toDto(document);
    }

    @Transactional
    public SuccessResponse supprimerDocument(String logementMasqueId, String documentMasqueId) {
        // Valider que le logement appartient à l'utilisateur
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        // Trouver le document par son ID
        Document document = documentRepository.findByMasqueId(documentMasqueId)
                .orElseThrow(() -> new SecurityException("Document introuvable."));
        // Dissocier le document du logement actuel
        if (!logement.getDocuments().remove(document)) {
            throw new IllegalArgumentException("Le document n'est pas associé à ce logement.");
        }
        logementRepository.save(logement);

        // Éviter les cycles en mettant à jour l'association inverse
        document.getLogements().remove(logement);
        // Vérifier si le document est encore utilisé par d'autres logements
        long nombreUtilisations = document.getLogements().size();

        if (nombreUtilisations == 0) {
            // Si aucun autre logement ne l'utilise, supprimer définitivement
            documentRepository.delete(document);
            return new SuccessResponse("Le document a été supprimé définitivement.");
        } else {
            // Si utilisé ailleurs, simplement dissocier
            return new SuccessResponse("Le document a été dissocié du logement.");
        }

    }



    private Logement validerLogementPourUtilisateur(String logementMasqueId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Accès interdit ou utilisateur introuvable."));
        Logement logement = logementRepository.findByMasqueId(logementMasqueId)
                .orElseThrow(() -> new SecurityException("Accès interdit ou logement introuvable."));
        if (!logement.getProprietaire().equals(utilisateur)) {
            throw new SecurityException("Accès interdit au logement.");
        }
        return logement;
    }

    @Transactional
    public SuccessResponse associerDocumentExistant(String logementMasqueId, String documentMasqueId) {
        Logement logement = validerLogementPourUtilisateur(logementMasqueId);
        Document document = documentRepository.findByMasqueId(documentMasqueId)
                .orElseThrow(() -> new SecurityException("Document introuvable."));
        if (logement.getDocuments().contains(document)) {
            throw new IllegalArgumentException("Le document est déjà associé à ce logement.");
        }
        logement.getDocuments().add(document);
        logementRepository.save(logement);
        return new SuccessResponse("Le document a été associé avec succès.");
    }
    public List<DocumentDTO> listerDocumentsDisponiblesPourUtilisateur() {
        // Récupérer l'utilisateur actuellement connecté
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Accès interdit ou utilisateur introuvable."));

        // Récupérer les logements de l'utilisateur
        List<Logement> logements = logementRepository.findByProprietaire(utilisateur);

        // Extraire les documents associés aux logements
        List<Document> documents = logements.stream()
                .flatMap(logement -> logement.getDocuments().stream())
                .distinct() // Éviter les doublons
                .toList();

        // Mapper les documents en DTO
        return documents.stream()
                .map(documentMapper::toDto)
                .toList();
    }
}
