package com.dhsp.luvu.service.impl;

import com.dhsp.luvu.dto.request.CollectionRequest;
import com.dhsp.luvu.dto.response.CollectionResponse;
import com.dhsp.luvu.entity.Collection;
import com.dhsp.luvu.repository.CollectionRepository;
import com.dhsp.luvu.service.CollectionService;
import com.dhsp.luvu.service.ProductService;
import com.dhsp.luvu.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    CollectionRepository collectionRepo;

    @Autowired
    ProductService productService;

    @Override
    public Collection save(CollectionRequest request) {
        try {
            if (collectionRepo.existsByName(request.getName()))
                throw new RuntimeException("Danh mục đã tồn tại!");

            String image = UploadUtils.save(request.getImage());

            Collection collection = new Collection();
            collection.setName(request.getName());
            collection.setImage(image);

            collection = collectionRepo.save(collection);
            return collection;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Collection update(CollectionRequest request, Long id) {

        try {
            Collection collection = collectionRepo.findById(id).orElseThrow(() -> new RuntimeException("Chưa có danh mục này trong hệ thống!"));

            if (request.getImage() != null) {
                String image = UploadUtils.save(request.getImage());
                collection.setImage(image);
            }

            collection.setName(request.getName());
            collection = collectionRepo.save(collection);

            return collection;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Không thể chỉnh sửa thành danh mục đã có trong hệ thống!");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Boolean delete(Long id) {
        try {
            collectionRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<CollectionResponse> findAll() {

        List<CollectionResponse> collectionResponses = new ArrayList<>();

        for (Collection collection : collectionRepo.findAll()) {
            collectionResponses.add(new CollectionResponse(collection.getId(), collection.getName(), collection.getImage(),
                    productService.findByCollectionId(collection.getId())));
        }

        return collectionResponses;
    }

    @Override
    public Collection findById(Long id) {
        return collectionRepo.findById(id).orElseThrow(() -> new RuntimeException("Chưa có danh mục này trong hệ thống."));
    }
}
