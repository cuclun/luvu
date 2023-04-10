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

    @Transactional
    @Modifying
    @Query("update Product p set p.quantity = p.quantity - :qty where p.id = :idProduct")
    void updateQuantity(Long idProduct, int qty);

    @Transactional
    @Modifying
    @Query("update Product p set p.name = :name, p.price = :price, p.description = :description, p.collection = :collection, p.quantity = :quantity where p.id = :idProduct")
    void updateProduct(String name, Long price, String description, Collection collection, int quantity, Long idProduct);

    List<Product> findAllByNameContaining(String name);
}
