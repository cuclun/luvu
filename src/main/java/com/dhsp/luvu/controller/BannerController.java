package com.dhsp.luvu.controller;

import com.dhsp.luvu.dto.request.BannerRequest;
import com.dhsp.luvu.dto.response.MessageResponse;
import com.dhsp.luvu.entity.Banner;
import com.dhsp.luvu.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/banner")
public class BannerController {

    @Autowired
    BannerService bannerService;

    @GetMapping(value = {"/", ""})
    public ResponseEntity<?> getBanners() {
        return new ResponseEntity<>(bannerService.findAll(), HttpStatus.OK);
    }

    @PostMapping(value = {"/", ""})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> postBanner(@ModelAttribute BannerRequest request) {
        try {
            Banner banner = bannerService.save(request);
            return new ResponseEntity<>(banner, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping (value = {"/{id}/", "/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBanner(@PathVariable Long id) {
        try {
            bannerService.delete(id);
            return new ResponseEntity<>(new MessageResponse("Xóa banner thành công"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = {"/{id}/", "/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBanner(@ModelAttribute BannerRequest bannerRequest, @PathVariable Long id) {
        try {
            bannerService.update(bannerRequest, id);
            return new ResponseEntity<>(new MessageResponse("Cập nhật banner thành công"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}