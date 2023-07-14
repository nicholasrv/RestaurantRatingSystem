package com.nicholasrv.restaurantratingsystem.repository;

import com.nicholasrv.restaurantratingsystem.model.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
}
