package com.dhsp.luvu.repository;

import com.dhsp.luvu.entity.Product;
import com.dhsp.luvu.entity.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecificationRepository extends JpaRepository<Specification, Long> {
    List<Specification> findAllByProduct(Product product);
}
