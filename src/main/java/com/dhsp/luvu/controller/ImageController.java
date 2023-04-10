package com.dhsp.luvu.controller;

import com.dhsp.luvu.dto.response.ImageResponse;
import com.dhsp.luvu.dto.response.MessageResponse;
import com.dhsp.luvu.entity.Image;
import com.dhsp.luvu.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/uploads")
public class ImageController {

    @Autowired
    private ImageRepository imageRepo;

    @PostMapping(value = {"/files", "/files/"})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] multipartFiles) {
        String message = "";
        for (MultipartFile multipartFile : multipartFiles) {
            HttpStatus httpStatus = uploadFile(multipartFile).getStatusCode();
            if (httpStatus == HttpStatus.BAD_REQUEST) {
                message = multipartFile.getOriginalFilename() + " already exists!";
                return new ResponseEntity<>(new MessageResponse(message), HttpStatus.BAD_REQUEST);
            } else if (httpStatus == HttpStatus.EXPECTATION_FAILED) {
                message = "Could not upload the file: " + multipartFile.getOriginalFilename() + "!";
                return new ResponseEntity<>(new MessageResponse(message), HttpStatus.EXPECTATION_FAILED);
            }
        }

        message = "Uploaded the list files successfully!";
        return new ResponseEntity<>(new MessageResponse(message), HttpStatus.OK);
    }

    @PostMapping(value = {"/file", "/file/"})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        String message = "";
        String name = multipartFile.getOriginalFilename();
        try {
            File file = new File("src/main/resources/uploads/" + name);
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(multipartFile.getBytes());
            }

            try {
                Image image = new Image(name);
                imageRepo.save(image);
            } catch (Exception e) {
                message = multipartFile.getOriginalFilename() + " already exists!";
                return new ResponseEntity<>(new MessageResponse(message), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            message = "Could not upload the file: " + multipartFile.getOriginalFilename() + "!";
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.EXPECTATION_FAILED);
        }

        Image image = imageRepo.findByName(name);
        return new ResponseEntity<>(new ImageResponse(image.getId(), "api/uploads/files/" + image.getId()), HttpStatus.OK);
    }

    @GetMapping(value = {"/files"}) // param product == NULL get all ELSE get image don't have product
    public ResponseEntity<?> getAllImage(@Param("product") Boolean product) {
        List<ImageResponse> imageResponseList = new ArrayList<>();
        List<Image> images;
        if (product == null || product == false) {
            images = imageRepo.findAll();
        } else {
            images = imageRepo.findAllByProduct(null);
        }

        for (Image image : images) {
            ImageResponse imageResponse = new ImageResponse(image.getId(), image.getName());
            imageResponseList.add(imageResponse);
        }

        return new ResponseEntity<>(imageResponseList, HttpStatus.OK);
    }

    @GetMapping(
            value = {"/files/{id}", "/files/{id}/"},
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    @ResponseBody
    public byte[] getImage(@PathVariable Long id) throws IOException {
        try {
            imageRepo.findById(id).get();
        } catch (Exception exception) {
            File file = new File("src/main/resources/uploads/404.jpg");
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return fileContent;
        }

        Image image = imageRepo.getById(id);

        File file = new File("src/main/resources/uploads/" + image.getName());
        byte[] fileContent = Files.readAllBytes(file.toPath());
        return fileContent;
    }

    @DeleteMapping(value = {"/{id}/", "/{id}"})
    @PreAuthorize("hasRole('MOD') or hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> deleteImage(@PathVariable Long id) {
        try {
            imageRepo.delete(imageRepo.findById(id).get());
            return new ResponseEntity<>(new MessageResponse("Delete image successfully!"), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(new MessageResponse("Image does not exist!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
