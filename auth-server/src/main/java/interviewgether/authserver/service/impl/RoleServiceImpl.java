package interviewgether.authserver.service.impl;


import interviewgether.authserver.model.Role;
import interviewgether.authserver.repository.RoleRepository;
import interviewgether.authserver.service.RoleService;
import interviewgether.authserver.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserService userService;

    public RoleServiceImpl(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @Override
    public Role create(Role role) {
        Assert.notNull(role, "Role cannot be null");
        return roleRepository.save(role);
    }

    @Override
    public Role readById(long id) {
        return roleRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role with Id " + id + " not found"));
    }

    @Override
    public Role update(Role updatedRole) {
        Assert.notNull(updatedRole, "Role cannot be null");
        readById(updatedRole.getId());
        return roleRepository.save(updatedRole);
    }

    @Override
    public void delete(long id) {
        readById(id);
        roleRepository.deleteById(id);
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findByRoleName(String name) {
        Assert.notNull(name, "Role name cannot be null");
        return roleRepository
                .findByRoleName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role " + name + " doesn't exists"));
    }
}
