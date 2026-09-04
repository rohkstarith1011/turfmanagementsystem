package com.crimsonlogic.turfmanagementsystem.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.crimsonlogic.turfmanagementsystem.entity.Role;
import com.crimsonlogic.turfmanagementsystem.repository.RoleRepository;

@Component
public class RoleDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleDataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        createRoleIfNotExists("PLAYER");
        createRoleIfNotExists("OWNER");
        createRoleIfNotExists("MANAGER");
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("COACH");
    }

    private void createRoleIfNotExists(String roleName) {

        if (!roleRepository.existsByRoleName(roleName)) {

            Role role = new Role();
            role.setRoleName(roleName);

            roleRepository.save(role);
        }
    }
}