package fr.qui.gestion.role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
	
	private final RoleRepository roleRepository;
	
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public Page<Role> obtenirTousLesRoles(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }
    public Optional<Role> obtenirRoleParId(Long id) {
        return roleRepository.findById(id);
    }
    public Role mettreAJourRole(Long id, Role detailsRole) {
    	Role role = roleRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Role avec ID " + id + " non trouvé"));
        role.setNom(detailsRole.getNom());
        return roleRepository.save(role);
    }
    public void supprimerRole(Long id) {
        // Vérifier si le role existe, puis le supprimer
        boolean exists = roleRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Role avec ID " + id + " non trouvé");
        }
        roleRepository.deleteById(id);
    }
}
