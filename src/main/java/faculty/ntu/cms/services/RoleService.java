package faculty.ntu.cms.services;

import faculty.ntu.cms.models.Post;
import faculty.ntu.cms.models.Role;
import faculty.ntu.cms.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
  private final RoleRepository roleRepository;

  @Autowired
  public RoleService(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }
  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }

  public Optional<Role> getRoleById(Integer id) {
    return roleRepository.findById(id);
  }

  public Role saveRole(Role role) {
    role.setCreatedAt(LocalDateTime.now());
    role.setUpdatedAt(LocalDateTime.now());
    return roleRepository.save(role);
  }

}
