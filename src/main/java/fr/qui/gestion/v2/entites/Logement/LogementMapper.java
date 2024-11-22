package fr.qui.gestion.v2.entites.Logement;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface LogementMapper {
    LogementDTO toDto(Logement logement);
}
