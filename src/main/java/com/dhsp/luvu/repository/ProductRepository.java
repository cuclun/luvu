package com.dhsp.luvu.repository;

import com.dhsp.luvu.entity.Collection;
import com.dhsp.luvu.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCollection(Collection collection);

    Boolean existsByName(String name);

    List<Product> findAllByNameContaining(String name);

    List<Product> findByCollection_Id(Long id);

    List<Product> findByQuantity(int quantity);
}
