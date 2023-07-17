package com.nicholasrv.restaurantratingsystem.repository;

import com.nicholasrv.restaurantratingsystem.model.Ratings;
import com.nicholasrv.restaurantratingsystem.model.Restaurants;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RatingsRepository extends MongoRepository<Ratings, String> {

    @Query("SELECT r FROM Ratings r WHERE r.restaurant = :restaurant")
    List<Ratings> findRatingsByRestaurant(Restaurants restaurants);
}
