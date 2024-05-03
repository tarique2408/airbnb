package com.airbnb.controller;

import com.airbnb.entity.Images;
import com.airbnb.repository.ImagesRepository;
import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.service.BucketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {
    private ImagesRepository imagesRepository;

    private PropertyRepository property;

    private BucketService bucketService;

    public ImageController(ImagesRepository imagesRepository, PropertyRepository property, BucketService bucketService) {
        this.imagesRepository = imagesRepository;
        this.property = property;
        this.bucketService = bucketService;
    }

   //http://localhost:8080/api/v1/images/upload/file/{bucketName}/propertyId/{propertyId}
    @PostMapping("/upload/file/{bucketName}/property/{propertyId}")
    public ResponseEntity<?> storeImage(@RequestParam MultipartFile file,
                                        @PathVariable String bucketName,
                                        @PathVariable long propertyId,
                                        @AuthenticationPrincipal PropertyUser user
                                        )
    {
        Property property1 = property.findById(propertyId).get();
        String imageUrl = bucketService.uploadFile(file, bucketName);
        Images img=new Images();
        img.setImageUrl(imageUrl);
        img.setProperty(property1);
        img.setPropertyUser(user);
        Images imageObject = imagesRepository.save(img);
        return new ResponseEntity<>(imageObject, HttpStatus.CREATED);
    }
}
