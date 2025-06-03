package fr.qui.gestion.v2.entites.Credit;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditMapper {
    CreditDTO toDto(Credit credit);
}
