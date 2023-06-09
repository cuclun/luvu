package com.dhsp.luvu.repository;

import com.dhsp.luvu.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Boolean existsByName(String roleName);

    Optional<Role> findByName(String roleName);

}
