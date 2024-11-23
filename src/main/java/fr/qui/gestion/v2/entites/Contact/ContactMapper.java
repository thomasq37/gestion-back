package fr.qui.gestion.v2.entites.Contact;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface ContactMapper {
    ContactDTO toDto(Contact contact);
}
