package fr.qui.gestion.user.role;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.qui.gestion.user.appuser.AppUser;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private List<AppUser> users;

}
