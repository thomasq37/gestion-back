package fr.qui.gestion.v2.entites.TotalCompte;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TotalCompteDTO {
    private String masqueId;
    private Double montant;
    private LocalDate dateEnregistrement;
}

