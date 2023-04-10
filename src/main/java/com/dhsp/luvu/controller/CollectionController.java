package com.dhsp.luvu.controller;

import com.dhsp.luvu.dto.request.CollectionRequest;
import com.dhsp.luvu.dto.response.*;
import com.dhsp.luvu.entity.Collection;
import com.dhsp.luvu.entity.Image;
import com.dhsp.luvu.entity.Product;
import com.dhsp.luvu.entity.Specification;
import com.dhsp.luvu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/collection")
public class CollectionController {

    @Autowired
    private CollectionRepository collectionRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private SpecificationRepository specificationRepo;

    @GetMapping(value = {"/", ""})
    public ResponseEntity<?> getCollections() {
        List<CollectionResponse> collectionResponses = new ArrayList<>();
        for (Collection collection : collectionRepo.findAllByOrderByIdAsc()) {
            CollectionResponse collectionResponse = getCollectionResponse(collection.getId());
            collectionResponses.add(collectionResponse);
        }
        return new ResponseEntity<>(collectionResponses, HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}/", "/{id}"})
    public ResponseEntity<?> getCollection(@PathVariable Long id) {
        try {
            collectionRepo.findById(id).get();

            return new ResponseEntity<>(getCollectionResponse(id), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(new MessageResponse("Collection does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = {"/", ""})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    public ResponseEntity<?> postCollection(@Valid @RequestBody CollectionRequest collectionRequest) {
        if (collectionRepo.findByName(collectionRequest.getName()) == null) {
            Image image = imageRepo.getById(collectionRequest.getImageID());
            collectionRepo.save(new Collection(collectionRequest.getName(), image));
            return new ResponseEntity<>(new MessageResponse("Add collection successfully!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Collection already exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = {"/{id}/", "/{id}"})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> deleteCollection(@PathVariable Long id) {
        try {
            collectionRepo.findById(id).get();
            Collection collection = collectionRepo.getById(id);
            collectionRepo.delete(collection);
            imageRepo.delete(collection.getImage());
            return new ResponseEntity<>(new MessageResponse("Delete collection successfully!"), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(new MessageResponse("Collection does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = {"/{id}/", "/{id}"})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> updateCollection(@Valid @RequestBody CollectionRequest collectionRequest, @PathVariable Long id) {
        try {
            collectionRepo.findById(id).get();
            Image image = imageRepo.getById(collectionRequest.getImageID());
            collectionRepo.updateCollection(id, collectionRequest.getName(), image);
            return new ResponseEntity<>(new MessageResponse("Update collection successfully!"), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(new MessageResponse("Collection does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public CollectionResponse getCollectionResponse(Long id) {
        Collection collection = collectionRepo.getById(id);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : productRepo.findAllByCollection(collection)) {
            List<Image> images = imageRepo.findAllByProduct(product);
            List<ImageResponse> imageResponses = new ArrayList<>();
            List<Specification> specifications = specificationRepo.findAllByProduct(product);
            List<SpecificationResponse> specificationResponses = new ArrayList<>();
            List<String> sizeRepository = new ArrayList<>();

            for (Image image : images) {
                ImageResponse imageResponse = new ImageResponse(image.getId(), "/api/uploads/files/" + image.getId());
                imageResponses.add(imageResponse);
            }

            for (Specification specification : specifications) {
                SpecificationResponse specificationResponse = new SpecificationResponse(specification.getId(), specification.getContent());
                specificationResponses.add(specificationResponse);
            }

            ProductResponse productResponse = new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getQuantity(), product.getDescription(), imageResponses, specificationResponses, collection.getId());
            productResponses.add(productResponse);
        }
        return new CollectionResponse(collection.getId(), collection.getName().toUpperCase(), productResponses, collection.getImage().getId());
    }
}
