package fr.qui.gestion.v2.entites.PeriodeDeLocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PeriodeDeLocationMapper {
    PeriodeDeLocationDTO toDto(PeriodeDeLocation periodeDeLocation);
}
