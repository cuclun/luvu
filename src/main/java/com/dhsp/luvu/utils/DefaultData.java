package com.dhsp.luvu.utils;

import com.dhsp.luvu.entity.Role;
import com.dhsp.luvu.entity.User;
import com.dhsp.luvu.repository.RoleRepository;
import com.dhsp.luvu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DefaultData implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        if (!roleRepository.existsByName("ROLE_MOD"))
            roleRepository.save(new Role("ROLE_MOD"));

        if (!roleRepository.existsByName("ROLE_ADMIN"))
            roleRepository.save(new Role("ROLE_ADMIN"));

        if (!userRepository.findByUsername("admin").isPresent()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAll());

            User admin = new User();

            admin.setName("Trần Thị Thanh Cúc");
            admin.setUsername("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(encoder.encode("admin"));
            admin.setRoles(roles);

            userRepository.save(admin);
        }
    }
}
