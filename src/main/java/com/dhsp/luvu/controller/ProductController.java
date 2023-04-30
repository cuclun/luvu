package com.dhsp.luvu.controller;

import com.dhsp.luvu.dto.request.ProductRequest;
import com.dhsp.luvu.dto.response.*;
import com.dhsp.luvu.entity.Collection;
import com.dhsp.luvu.entity.Image;
import com.dhsp.luvu.entity.Product;
import com.dhsp.luvu.entity.Specification;
import com.dhsp.luvu.repository.*;
import com.dhsp.luvu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("Sản phẩm không tồn tại!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = {"/", ""})
    public ResponseEntity<?> search(@Param("keyword") String keyword) {
        return new ResponseEntity<>(productService.search(keyword), HttpStatus.OK);
    }

    @PostMapping(value = {"", "/"})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    public ResponseEntity<?> postProduct(@ModelAttribute ProductRequest request) {
        try {
            return new ResponseEntity<>(productService.save(request), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = {"/{id}/", "/{id}"})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {

        try {
            productService.delete(id);
            return new ResponseEntity<>(new MessageResponse("Xóa sản phẩm thành công."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = {"/{id}/", "/{id}"})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@ModelAttribute ProductRequest request, @PathVariable Long id) {
        try {
            productService.update(request, id);
            return new ResponseEntity<>(new MessageResponse("Cập nhật sản phẩm thành công."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
