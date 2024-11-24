package fr.qui.gestion.v2.entites.Locataire;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocataireMapper {
    LocataireDTO toDto(Locataire locataire);
}
