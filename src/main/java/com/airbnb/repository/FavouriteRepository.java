package com.airbnb.repository;

import com.airbnb.entity.Favourite;
import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    @Query("select f from Favourite f where f.property=:property and f.propertyUser=:propertyUser")
    Favourite findByPropertyAndPropertyUser(@Param("property") Property property,@Param("propertyUser") PropertyUser propertyUser);
}