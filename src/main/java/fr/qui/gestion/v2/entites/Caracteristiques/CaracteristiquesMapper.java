package fr.qui.gestion.v2.entites.Caracteristiques;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CaracteristiquesMapper {
    CaracteristiquesDTO toDto(Caracteristiques caracteristiques);
}
