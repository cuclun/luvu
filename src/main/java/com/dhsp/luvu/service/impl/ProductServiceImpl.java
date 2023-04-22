package com.dhsp.luvu.service.impl;

import com.dhsp.luvu.dto.request.ProductRequest;
import com.dhsp.luvu.dto.response.ImageResponse;
import com.dhsp.luvu.dto.response.ProductResponse;
import com.dhsp.luvu.dto.response.SpecificationResponse;
import com.dhsp.luvu.entity.Image;
import com.dhsp.luvu.entity.Product;
import com.dhsp.luvu.entity.Specification;
import com.dhsp.luvu.repository.CollectionRepository;
import com.dhsp.luvu.repository.ImageRepository;
import com.dhsp.luvu.repository.ProductRepository;
import com.dhsp.luvu.repository.SpecificationRepository;
import com.dhsp.luvu.service.ProductService;
import com.dhsp.luvu.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    CollectionRepository collectionRepo;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    SpecificationRepository specificationRepo;

    @Autowired
    ImageRepository imageRepo;

    @Override
    public ProductResponse findById(Long id) {
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
            if (content.contains("Size")) {
                String sizes = content.substring(5);
                sizeResponses = Arrays.asList(sizes.split("–"));
            }

            if (content.contains("Màu")) {
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

    @Override
    public List<ProductResponse> search(String keyword) {
        List<Product> products = productRepo.findAllByNameContaining(keyword);
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products) {
            ProductResponse productResponse = findById(product.getId());
            productResponses.add(productResponse);
        }
        return productResponses;
    }

    @Override
    public List<ProductResponse> findByCollectionId(Long id) {
        List<Product> products = productRepo.findByCollection_Id(id);
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products) {
            ProductResponse productResponse = findById(product.getId());
            productResponses.add(productResponse);
        }
        return productResponses;
    }

    @Override
    @Transactional
        public ProductResponse save(ProductRequest request) {
            if (productRepo.existsByName(request.getName()))
                throw new RuntimeException("Sản phẩm đã tồn tại trong hệ thống!");

            Product product = new Product();
            product.setName(request.getName());
            product.setPrice(request.getPrice());
            product.setQuantity(request.getQuantity());
        product.setDescription(request.getDescription());
        product.setCollection(collectionRepo.findById(request.getCollectionId()).orElseThrow(() -> new RuntimeException("Danh mục không tồn tại!")));

        try {
            product = productRepo.save(product);
            for (MultipartFile image : request.getImages()) {
                String nameImage = UploadUtils.save(image);
                imageRepo.save(new Image(nameImage, product));
            }

            for (String specification : request.getSpecifications()) {
                specificationRepo.save(new Specification(specification, product));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return findById(product.getId());
    }

    @Override
    @Transactional
    public ProductResponse update(ProductRequest request, Long id) {
        Product product = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Sản phẩm này không tồn tại!"));
        try {
            specificationRepo.deleteAllByProduct(product);

            product.setName(request.getName());
            product.setPrice(request.getPrice());
            product.setQuantity(request.getQuantity());
            product.setDescription(request.getDescription());
            product.setCollection(collectionRepo.findById(request.getCollectionId()).orElseThrow(() -> new RuntimeException("Danh mục không tồn tại!")));

            productRepo.save(product);

            for (String specification : request.getSpecifications()) {
                specificationRepo.save(new Specification(specification, product));
            }

            return findById(id);
        }  catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Không thể chỉnh sửa thành sản phẩm đã có trong hệ thống!");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Boolean delete(Long id) {
        Product product = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Sản phẩm này không tồn tại!"));
        try {
            specificationRepo.deleteAllByProduct(product);
            imageRepo.deleteAllByProduct(product);
            productRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
