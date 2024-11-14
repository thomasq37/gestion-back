package fr.qui.gestion.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TentativeBlocageService {

    private final TentativeBlocageRepository repository;

    public TentativeBlocageService(TentativeBlocageRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public boolean peutTenterConnexion(String ip) {
        return peutTenter(ip, "CONNEXION", 1500, 2); // 5 tentatives, bloqué pour 2 heures
    }

    @Transactional
    public boolean peutTenterInscription(String ip) {
        return peutTenter(ip, "INSCRIPTION", 1300, 1); // 3 tentatives, bloqué pour 1 heure
    }

    private boolean peutTenter(String ip, String type, int maxTentatives, long blocageHeures) {
        Optional<TentativeBlocage> tentativeOpt = repository.findByIpAndType(ip, type);
        TentativeBlocage tb;

        if (tentativeOpt.isPresent()) {
            tb = tentativeOpt.get();
            if (tb.getDeblocageApres() != null && LocalDateTime.now().isBefore(tb.getDeblocageApres())) {
                return false; // IP est actuellement bloquée
            }
        } else {
            tb = new TentativeBlocage();
            tb.setIp(ip);
            tb.setType(type);
        }

        tb.setTentatives(tb.getTentatives() + 1);
        tb.setDernierEssai(LocalDateTime.now());

        if (tb.getTentatives() > maxTentatives) {
            tb.setDeblocageApres(LocalDateTime.now().plusHours(blocageHeures));
            tb.setTentatives(0); // Optionnel, dépend de comment vous voulez gérer après le blocage
        }

        repository.save(tb);
        return true;
    }
}
