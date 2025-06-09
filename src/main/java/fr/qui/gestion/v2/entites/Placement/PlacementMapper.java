package fr.qui.gestion.v2.entites.Placement;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlacementMapper {
    PlacementVueEnsembleDTO toVueEnsembleDTO(Placement placement);
}
