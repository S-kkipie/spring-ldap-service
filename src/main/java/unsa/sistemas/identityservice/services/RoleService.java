package unsa.sistemas.identityservice.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import unsa.sistemas.identityservice.models.Role;
import unsa.sistemas.identityservice.repositories.IRoleRepository;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class RoleService {
    private final IRoleRepository roleRepository;

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> findAllRole() {
        return roleRepository.findAll();
    }

    public Role findDefaultRole() {
        return findAllRole().stream().findFirst().orElse(null);
    }

    public Role findRoleByName(String role) {
        return findAllRole().stream().filter(r -> r.getName().equals(role)).findFirst().orElse(null);
    }
}
