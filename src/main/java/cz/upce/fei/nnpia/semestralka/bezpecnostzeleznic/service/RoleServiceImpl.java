package cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service;

import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.Role;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.model.RoleName;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.repository.RoleRepository;
import cz.upce.fei.nnpia.semestralka.bezpecnostzeleznic.service.interfaces.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(RoleName roleName) {
        return roleRepository.findByName(roleName).
                orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role nebyla nalezena."));
    }

}
