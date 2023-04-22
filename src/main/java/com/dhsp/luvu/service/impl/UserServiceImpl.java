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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
            throw new RuntimeException("Fail -> Us88ername is already taken!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Fail -> Email is already in use!");
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
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
