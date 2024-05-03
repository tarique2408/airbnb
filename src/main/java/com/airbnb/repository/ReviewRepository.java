package com.airbnb.repository;

import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    //find review for a user on a property
    //@Param- bind the method parameter to name parameter in query
    @Query("select r from Review r where r.property=:property and r.propertyUser=:propertyUser")
   Review findReviewByUserAndProperty(@Param("property") Property property,@Param("propertyUser") PropertyUser propertyUser);

    @Query("select r from Review r where r.propertyUser=:propertyUser")
    List<Review> findByPropertyUser(@Param("propertyUser") PropertyUser propertyUser);
}