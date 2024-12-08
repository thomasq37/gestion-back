package fr.qui.gestion.v2.entites.Photo;
import lombok.Data;

@Data
public class PhotoDTO {
    private String masqueId;
    private Boolean isPrincipal;
    private byte[] image;

}