package fr.qui.gestion.v2.entites.TotalCompte;

import fr.qui.gestion.v2.entites.Placement.Placement;
import fr.qui.gestion.v2.entites.Placement.PlacementVueEnsembleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TotalCompteMapper {
    TotalCompteDTO toVueEnsembleDTO(TotalCompte totalCompte);
}
