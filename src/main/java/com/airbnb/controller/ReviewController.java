package com.airbnb.controller;

import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.entity.Review;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.PropertyUserRepository;
import com.airbnb.repository.ReviewRepository;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private ReviewRepository reviewRepository;

    private PropertyRepository propertyRepository;
    public ReviewController(ReviewRepository reviewRepository, PropertyRepository propertyRepository) {
        this.reviewRepository = reviewRepository;

        this.propertyRepository = propertyRepository;
    }

    //1.Add review on property

    //url-http://localhost:8080/api/v1/reviews/addReview/propertyId (why propertyId - to add review on property)
    @PostMapping("/addReview/{propertyId}")
    public ResponseEntity<?> addReview(
            @AuthenticationPrincipal PropertyUser user,
            @PathVariable long propertyId,
            @RequestBody Review review
            ){
        //first get the property by using propertyId and then

        Property property = propertyRepository.findById(propertyId).get();
        Review userReview = reviewRepository.findReviewByUserAndProperty(property, user);
        if (userReview!=null){
            return new ResponseEntity<>("Already review is given",HttpStatus.BAD_REQUEST);
        }
        review.setProperty(property);//set propertyId
        review.setPropertyUser(user);//set  userId
        reviewRepository.save(review);
        return new ResponseEntity<>("Review has been added successfully", HttpStatus.CREATED);
    }

    //2-Get all user review
    //URL-http://localhost:8080/api/v1/reviews/allReviewOfUser
    @GetMapping("/allReviewOfUser")
    public ResponseEntity<?> getAllReviewOfUser(@AuthenticationPrincipal PropertyUser user)
    {
        List<Review> byPropertyUser = reviewRepository.findByPropertyUser(user);
        return new ResponseEntity<>(byPropertyUser,HttpStatus.OK);
    }


    //3-delete a review
    //url-http://localhost:8080/api/v1/reviews/deleteReview/propertyId
    @DeleteMapping("/deleteReview/{propertyId}")
    public ResponseEntity<?> deleteReview(@PathVariable long propertyId
                                          ,@AuthenticationPrincipal PropertyUser user
    ){
        Property property = propertyRepository.findById(propertyId).get();

        Review r = reviewRepository.findReviewByUserAndProperty(property, user);
        reviewRepository.deleteById(r.getId());
        return new ResponseEntity<>("Review has been deleted successfully",HttpStatus.OK);
    }

    //4-update review
    //to update the review first get the review and then override with new content and set to the review
    //1- get the review of a user on a property
    //url-http://localhost:8080/api/v1/reviews/getReview/propertyId
    @GetMapping("/getReview/{propertyId}")
    public ResponseEntity<?> getReview(@PathVariable long propertyId,@AuthenticationPrincipal PropertyUser propertyUser){
        Optional<Property> byId = propertyRepository.findById(propertyId);
        Property property = byId.get();

        Review review = reviewRepository.findReviewByUserAndProperty(property, propertyUser);
        return new ResponseEntity<>(review,HttpStatus.OK);
    }

    //2-now update the review
    //url-http://localhost:8080/api/v1/reviews/updateReview/propertyId
    @PutMapping("/updateReview/{propertyId}")
    public ResponseEntity<?> updateReview(@PathVariable long propertyId,
                                          @AuthenticationPrincipal PropertyUser user,
                                          @RequestBody Review review
                                          ){
        Property property = propertyRepository.findById(propertyId).get();
        Review review1 = reviewRepository.findReviewByUserAndProperty(property, user);
        review1.setContent(review.getContent());
        review1.setPropertyUser(user);
        review1.setProperty(propertyRepository.findById(propertyId).get());
        Review save = reviewRepository.save(review1);
        return new ResponseEntity<>(save,HttpStatus.OK);
    }

}
