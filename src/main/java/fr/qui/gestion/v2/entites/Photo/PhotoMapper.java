package fr.qui.gestion.v2.entites.Photo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhotoMapper {
    PhotoDTO toDto(Photo photo);
}
