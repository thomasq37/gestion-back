package fr.qui.gestion.v2.entites.Frais;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FraisMapper {
    FraisDTO toDto(Frais frais);
}
