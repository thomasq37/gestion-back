package fr.qui.gestion.v2.entites.document;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentDTO toDto(Document document);
    Document toEntity(DocumentDTO documentDTO);
}

