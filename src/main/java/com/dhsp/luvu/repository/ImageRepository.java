package com.dhsp.luvu.repository;

import com.dhsp.luvu.entity.Image;
import com.dhsp.luvu.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByProduct(Product product);

    Image findByName(String name);

    void deleteAllByProduct(Product product);

    @Transactional
    @Modifying
    @Query("update Image i set i.product = :product where i.id = :idImage")
    void updateImage(Long idImage, Product product);
}
