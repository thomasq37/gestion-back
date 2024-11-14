package fr.qui.gestion.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TentativeBlocageRepository extends JpaRepository<TentativeBlocage, Long> {
    Optional<TentativeBlocage> findByIpAndType(String ip, String type);
}
