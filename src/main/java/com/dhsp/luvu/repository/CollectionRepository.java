package com.dhsp.luvu.repository;

import com.dhsp.luvu.entity.Collection;
import com.dhsp.luvu.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    Boolean existsByName(String name);

    Collection findByName(String name);

    List<Collection> findByOrderByIdAsc();
}
