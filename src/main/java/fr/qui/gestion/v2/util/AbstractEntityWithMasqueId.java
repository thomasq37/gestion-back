package fr.qui.gestion.v2.util;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntityWithMasqueId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String masqueId;

    @PrePersist
    private void prePersist() {
        if (this.masqueId == null) {
            this.masqueId = genererMasqueId(this.getClass().getSimpleName(), System.nanoTime());
        }
    }

    @PostLoad
    private void postLoad() {
        if (this.masqueId == null && this.id != null) {
            this.masqueId = genererMasqueId(this.getClass().getSimpleName(), this.id);
        }
    }

    private String genererMasqueId(String className, Long id) {
        int classHash = Math.abs(className.hashCode() % 1_000);
        long uniqueId = (classHash * 1_000_000L) + id;
        return String.valueOf(uniqueId);
    }
}
