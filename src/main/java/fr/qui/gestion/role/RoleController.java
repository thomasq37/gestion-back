package fr.qui.gestion.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/roles")
@CrossOrigin(origins = "${app.cors.origin}")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    
    // Obtenir tous les roles avec pagination
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<Role>> obtenirTousLesRoles(Pageable pageable) {
        Page<Role> roles = roleService.obtenirTousLesRoles(pageable);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    // Obtenir un role par son ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Role> obtenirRoleParId(@PathVariable Long id) {
    	Role role = roleService.obtenirRoleParId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role non trouvé"));
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    // Ajouter un role
    /*@PostMapping
    public ResponseEntity<Role> ajouterRole(@RequestBody Role role) {
    	Role roleEnregistre = roleService.ajouterRole(role);
        return new ResponseEntity<>(roleEnregistre, HttpStatus.CREATED);
    }*/

    // Mettre à jour un role
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Role> mettreAJourRole(@PathVariable Long id, @RequestBody Role detailsRole) {
    	Role roleMisAJour = roleService.mettreAJourRole(id, detailsRole);
        return new ResponseEntity<>(roleMisAJour, HttpStatus.OK);
    }

    // Supprimer un role
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> supprimerRole(@PathVariable Long id) {
    	roleService.supprimerRole(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
