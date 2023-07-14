package com.nicholasrv.restaurantratingsystem.repository;

import com.nicholasrv.restaurantratingsystem.model.Ratings;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RatingsRepository extends MongoRepository<Ratings, String> {
}
