package com.airbnb.controller;

import com.airbnb.entity.Property;
import com.airbnb.repository.CountryRepository;
import com.airbnb.repository.LocationRepository;
import com.airbnb.repository.PropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {

    private PropertyRepository propertyRepository;


    public PropertyController(PropertyRepository propertyRepository) {

        this.propertyRepository = propertyRepository;
    }



    //url-http://localhost:8080/api/v1/properties/locationName
    @GetMapping("/{locationName}")
    public ResponseEntity<List<Property>> getProperty(
            @PathVariable String locationName
    ){
        List<Property> properties = propertyRepository.findPropertyByLocation(locationName);
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }
    //create property
    //url-http://localhost:8080/api/v1/properties/addProperty

    //instead of using nested json we can pass the id of location and country in url and get the object of these
    //entity based on id and set in the property object

    @PostMapping("/addProperty")
    public ResponseEntity<?> addProperty(@RequestBody Property property){


        Property save = propertyRepository.save(property);
        return new ResponseEntity<>(save,HttpStatus.CREATED);
    }

    //delete the property
    //url-http://localhost:8080/api/v1/properties/delete/propertyId
    @DeleteMapping("/delete/{propertyId}")
    public ResponseEntity<?> deleteProperty(@PathVariable long propertyId){
        propertyRepository.deleteById(propertyId);
        return new ResponseEntity<>("property has been deleted!",HttpStatus.OK);
    }


}
