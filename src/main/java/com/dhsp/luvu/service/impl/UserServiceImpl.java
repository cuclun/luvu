package com.dhsp.luvu.service.impl;

import com.dhsp.luvu.dto.request.SignupRequest;
import com.dhsp.luvu.dto.response.MessageResponse;
import com.dhsp.luvu.entity.Role;
import com.dhsp.luvu.entity.User;
import com.dhsp.luvu.repository.RoleRepository;
import com.dhsp.luvu.repository.UserRepository;
import com.dhsp.luvu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public User register(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("Username đã tồn tại!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        User user = new User();

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_MOD").get());

        user.setEmail(signupRequest.getEmail());
        user.setUsername(signupRequest.getUsername());
        user.setName(signupRequest.getName());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public Boolean delete(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<User> findAll() {
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").get();
        List<User> userMod = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            if (!user.getRoles().contains(roleAdmin)) {
                userMod.add(user);
            }
        }
        return userMod;
    }

}
