package com.nicholasrv.restaurantratingsystem.service;

import com.mongodb.MongoException;
import com.nicholasrv.restaurantratingsystem.RestaurantRatingSystem;
import com.nicholasrv.restaurantratingsystem.model.Ratings;
import com.nicholasrv.restaurantratingsystem.repository.RatingsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingsServiceImpl implements RestaurantRatingSystem<Ratings> {

    private final RatingsRepository ratingsRepository;

    public RatingsServiceImpl(RatingsRepository ratingsRepository) {
        this.ratingsRepository = ratingsRepository;
    }

    @Override
    public Ratings save(Ratings ratings) {
        if(ratings != null){
            return ratingsRepository.save(ratings);
        }
        return new Ratings();
    }

    @Override
    public String update(Ratings ratings) {
        if(ratings != null && ratingsRepository.findById(ratings.getId()).isPresent()){
            ratingsRepository.save(ratings);
            return "Rating successfully updated!";
        }
        return "Sorry, but the requested rating couldn't be found.";
    }

    @Override
    public List<Ratings> getAllResults() throws MongoException {
        return ratingsRepository.findAll();
    }

    @Override
    public Optional<Ratings> searchById(String id) throws MongoException {
        return ratingsRepository.findById(id);
    }

    @Override
    public boolean delete(String id) throws MongoException {
        if(ratingsRepository.findById(id).isPresent()){
            ratingsRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
