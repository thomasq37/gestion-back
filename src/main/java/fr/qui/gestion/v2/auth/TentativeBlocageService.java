package fr.qui.gestion.v2.auth;

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
        return peutTenter(ip, "CONNEXION", 1500, 2);
    }

    @Transactional
    public boolean peutTenterInscription(String ip) {
        return peutTenter(ip, "INSCRIPTION", 1300, 1);
    }

    private boolean peutTenter(String ip, String type, int maxTentatives, long blocageHeures) {
        Optional<TentativeBlocage> tentativeOpt = repository.findByIpAndType(ip, type);
        TentativeBlocage tb;

        if (tentativeOpt.isPresent()) {
            tb = tentativeOpt.get();
            if (tb.getDeblocageApres() != null && LocalDateTime.now().isBefore(tb.getDeblocageApres())) {
                return false;
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
            tb.setTentatives(0);
        }

        repository.save(tb);
        return true;
    }
}
