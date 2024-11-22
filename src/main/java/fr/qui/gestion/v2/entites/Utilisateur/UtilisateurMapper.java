package fr.qui.gestion.v2.entites.Utilisateur;

import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface UtilisateurMapper {
    UtilisateurDTO toDto(Utilisateur utilisateur);
}
