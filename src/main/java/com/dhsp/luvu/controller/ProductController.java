package com.dhsp.luvu.controller;

import com.dhsp.luvu.dto.request.ProductRequest;
import com.dhsp.luvu.dto.response.*;
import com.dhsp.luvu.entity.Collection;
import com.dhsp.luvu.entity.Image;
import com.dhsp.luvu.entity.Product;
import com.dhsp.luvu.entity.Specification;
import com.dhsp.luvu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    CollectionRepository collectionRepo;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    SpecificationRepository specificationRepo;

    @Autowired
    ImageRepository imageRepo;

    @PostMapping(value = {"", "/"})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    public ResponseEntity<?> postProduct(@Valid @RequestBody ProductRequest productRequest) {
        Collection collection = collectionRepo.getById(productRequest.getCollectionId());
        Product product = new Product(productRequest.getName().toLowerCase(), productRequest.getPrice(), productRequest.getDescription(), productRequest.getQuantity(), collection);
        try {
            product = productRepo.save(product);
            for (String content : productRequest.getSpecifications()) {
                Specification specification = new Specification(content, product);
                specificationRepo.save(specification);
            }
            for (Long idImage : productRequest.getImagesId()) {
                imageRepo.updateImage(idImage, product);
            }
            return new ResponseEntity<>(new MessageResponse("Add product successfully!"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("Product already exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            productRepo.findById(id).get();

            return new ResponseEntity<>(getProductResponse(id), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(new MessageResponse("Product does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/", ""})
    public ResponseEntity<?> getProducts(@Param("keyword") String keyword) {
        List<ProductResponse> productResponses = new ArrayList<>();
        List<Product> products;
        if (keyword == null || keyword.equals("")) {
            products = productRepo.findAll();
        } else {
            products = productRepo.findAllByNameContaining(keyword);
        }

        for (Product product : products) {
            ProductResponse productResponse = getProductResponse(product.getId());
            productResponses.add(productResponse);
        }
        return new ResponseEntity<>(productResponses, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/{id}/", "/{id}"})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> deleteCollection(@PathVariable Long id) {
        try {
            Product product = productRepo.findById(id).get();
            List<Image> images = imageRepo.findAllByProduct(product);
            for (Image image : images) {
                imageRepo.delete(image);
            }
            List<Specification> specifications = specificationRepo.findAllByProduct(product);
            for (Specification specification : specifications) {
                specificationRepo.delete(specification);
            }
            productRepo.delete(product);
            return new ResponseEntity<>(new MessageResponse("Delete product successfully!"), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(new MessageResponse("Product does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = {"/{id}/", "/{id}"})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductRequest productRequest, @PathVariable Long id) {
        try {
            Product product = productRepo.getById(id);
            Collection collection = collectionRepo.getById(productRequest.getCollectionId());
            List<Specification> specifications = specificationRepo.findAllByProduct(product);
            for (Specification specification : specifications) {
                specificationRepo.delete(specification);
            }
            for (String content : productRequest.getSpecifications()) {
                Specification specification = new Specification(content, product);
                specificationRepo.save(specification);
            }
            productRepo.updateProduct(productRequest.getName(), productRequest.getPrice(),
                    productRequest.getDescription(), collection,
                    productRequest.getQuantity(), id);

            return new ResponseEntity<>(new MessageResponse("Update product successfully!"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("Update product error!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ProductResponse getProductResponse(Long id) {
        Product product = productRepo.getById(id);
        List<Image> imageList = imageRepo.findAllByProduct(product);
        List<ImageResponse> imageResponses = new ArrayList<>();
        List<Specification> specifications = specificationRepo.findAllByProduct(product);
        List<SpecificationResponse> specificationResponses = new ArrayList<>();
        List<String> sizeResponses = new ArrayList<>();
        List<String> colorResponses = new ArrayList<>();

        for (Image image : imageList) {
            ImageResponse imageResponse = new ImageResponse(image.getId(), image.getName());
            imageResponses.add(imageResponse);
        }

        for (Specification specification : specifications) {
            String content = specification.getContent();
            SpecificationResponse specificationResponse = new SpecificationResponse(specification.getId(), content);
            specificationResponses.add(specificationResponse);
            content = content.replaceAll(" ", "");
            if (content.indexOf("Size") != -1) {
                String sizes = content.substring(5);
                sizeResponses = Arrays.asList(sizes.split("-"));
            }

            if (content.indexOf("Màu") != -1) {
                String colors = content.substring(4);
                colorResponses = Arrays.asList(colors.split(","));
            }
        }

        return new ProductResponse(
                product.getId(),
                product.getName().toUpperCase(),
                product.getPrice(),
                product.getQuantity(),
                product.getDescription(),
                imageResponses,
                specificationResponses,
                product.getCollection().getId(),
                sizeResponses,
                colorResponses);
    }
}
