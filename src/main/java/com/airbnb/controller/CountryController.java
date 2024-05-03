package com.airbnb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {
    //url-http://localhost:8080/api/v1/countries/addCountry

    @PostMapping("/addCountry")
    public String addCountry(){
        return "done";
    }
}
