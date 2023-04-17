package com.dhsp.luvu.controller;

import com.dhsp.luvu.dto.request.CollectionRequest;
import com.dhsp.luvu.dto.response.*;
import com.dhsp.luvu.entity.Collection;
import com.dhsp.luvu.entity.Image;
import com.dhsp.luvu.entity.Product;
import com.dhsp.luvu.entity.Specification;
import com.dhsp.luvu.repository.*;
import com.dhsp.luvu.service.CollectionService;
import com.dhsp.luvu.service.ProductService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/collection")
public class CollectionController {

    @Autowired
    CollectionService collectionService;

    @Autowired
    ProductService productService;

    @GetMapping(value = {"/", ""})
    public ResponseEntity<?> getCollections() {
        return new ResponseEntity<>(collectionService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}/", "/{id}"})
    public ResponseEntity<?> findByCollection(@PathVariable Long id) {
        try {
            Collection collection = collectionService.findById(id);
            CollectionResponse collectionResponse = new CollectionResponse();
            collectionResponse.setName(collection.getName());
            collectionResponse.setId(collection.getId());
            collectionResponse.setImage(collection.getImage());
            collectionResponse.setProducts(productService.findByCollectionId(id));

            return new ResponseEntity<>(collectionResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = {"/", ""})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    public ResponseEntity<?> postCollection(@ModelAttribute CollectionRequest request) {
        try {
            Collection collection = collectionService.save(request);
            return new ResponseEntity<>(collection, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = {"/{id}/", "/{id}"})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteCollection(@PathVariable Long id) {
        try {
            collectionService.delete(id);
            return new ResponseEntity<>(new MessageResponse("Xóa danh mục thành công!"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = {"/{id}/", "/{id}"})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    public ResponseEntity<?> updateCollection(@ModelAttribute CollectionRequest collectionRequest, @PathVariable Long id) {
        try {
            collectionService.update(collectionRequest, id);
            return new ResponseEntity<>(new MessageResponse("Cập nhật danh mục thành công!"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
