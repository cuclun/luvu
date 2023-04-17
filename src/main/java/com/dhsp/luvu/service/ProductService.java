package com.dhsp.luvu.service;

import com.dhsp.luvu.dto.request.ProductRequest;
import com.dhsp.luvu.dto.response.ProductResponse;
import com.dhsp.luvu.entity.Product;

import java.util.List;

public interface ProductService {
    ProductResponse findById(Long id);

    List<ProductResponse> search(String keyword);

    List<ProductResponse> findByCollectionId(Long id);

    ProductResponse save(ProductRequest request);

    ProductResponse update(ProductRequest request, Long id);

    Boolean delete(Long id);
}
