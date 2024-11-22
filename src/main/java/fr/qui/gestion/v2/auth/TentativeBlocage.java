package fr.qui.gestion.v2.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class TentativeBlocage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ip;
    private int tentatives;
    private LocalDateTime dernierEssai;
    private LocalDateTime deblocageApres;
    private String type;
}
