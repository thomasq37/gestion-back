package fr.qui.gestion.v2.entites.Adresse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdresseMapper {
    AdresseDTO toDto(Adresse adresse);
}
