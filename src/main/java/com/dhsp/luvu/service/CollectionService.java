package com.dhsp.luvu.service;

import com.dhsp.luvu.dto.request.CollectionRequest;
import com.dhsp.luvu.entity.Collection;

import java.util.List;

public interface CollectionService {
    Collection save(CollectionRequest request);

    Collection update(CollectionRequest request, Long id);

    Boolean delete(Long id);

    List<Collection> findAll();

    Collection findById(Long id);
}
