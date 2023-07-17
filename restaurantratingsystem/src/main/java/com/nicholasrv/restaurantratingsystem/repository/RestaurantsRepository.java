package com.nicholasrv.restaurantratingsystem.repository;

import com.nicholasrv.restaurantratingsystem.model.Restaurants;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantsRepository extends MongoRepository<Restaurants, String> {
    public boolean existsByName(String name);
    public boolean existsById(String id);
}
