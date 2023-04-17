package com.dhsp.luvu.controller;

import com.dhsp.luvu.entity.Image;
import com.dhsp.luvu.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/uploads")
public class ImageController {

    @Autowired
    ImageRepository imageRepo;

    @GetMapping(
            value = {"/{id}", "/{id}/"},
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    @ResponseBody
    public byte[] getImage(@PathVariable Long id) {
        Image image = imageRepo.findById(id).orElse(new Image("404.jpg"));
        return getImage(image.getName());
    }

    @GetMapping(
            value = {"/image/{name}", "/image/{name}/"},
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    @ResponseBody
    public byte[] getImage(@PathVariable String name) {
        try {
            File file = new File("src/main/resources/uploads/" + name);
            return Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            return getImage("404.jpg");
        }
    }
}
