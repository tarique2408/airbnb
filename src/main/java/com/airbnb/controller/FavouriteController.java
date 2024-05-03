package com.airbnb.controller;

import com.airbnb.entity.Favourite;
import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.FavouriteRepository;
import com.airbnb.repository.PropertyRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/favourite")
public class FavouriteController {
    private FavouriteRepository favouriteRepository;
    private PropertyRepository propertyRepository;

    public FavouriteController(FavouriteRepository favouriteRepository, PropertyRepository propertyRepository) {
        this.favouriteRepository = favouriteRepository;
        this.propertyRepository = propertyRepository;
    }

    //1-create favourite
    //url-http://localhost:8080/api/v1/favourite
    // we will take property object from postman as JSON nested
    @PostMapping
    public ResponseEntity<Favourite> createFavourite(@RequestBody Favourite favourite,
                                                     @AuthenticationPrincipal PropertyUser user){
        favourite.setPropertyUser(user);
        Favourite saved = favouriteRepository.save(favourite);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    //2.delete/update
    @DeleteMapping("/{propertyId}")
    public ResponseEntity<?> deleteFav(@PathVariable long propertyId, @AuthenticationPrincipal PropertyUser user)
    {
        Optional<Property> byId = propertyRepository.findById(propertyId);
        Property property = byId.get();
        Favourite fav = favouriteRepository.findByPropertyAndPropertyUser(property, user);
        favouriteRepository.deleteById(fav.getId());
        return new ResponseEntity<>("property removed from fav",HttpStatus.OK);
    }

}
