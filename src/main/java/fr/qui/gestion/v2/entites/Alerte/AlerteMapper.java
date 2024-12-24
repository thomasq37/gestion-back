package fr.qui.gestion.v2.entites.Alerte;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface AlerteMapper {
    AlerteDTO toDto(Alerte alerte);
}
