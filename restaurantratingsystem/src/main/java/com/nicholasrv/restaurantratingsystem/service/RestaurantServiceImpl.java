package com.nicholasrv.restaurantratingsystem.service;

import com.mongodb.MongoException;
import com.nicholasrv.restaurantratingsystem.RestaurantRatingSystem;
import com.nicholasrv.restaurantratingsystem.model.Restaurants;
import com.nicholasrv.restaurantratingsystem.repository.RestaurantsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantRatingSystem<Restaurants> {
    private final RestaurantsRepository restaurantsRepository;

    public RestaurantServiceImpl(RestaurantsRepository restaurantsRepository) {
        this.restaurantsRepository = restaurantsRepository;
    }

    @Override
    public Restaurants save(Restaurants restaurants) {
        if(restaurants != null){
            return restaurantsRepository.save(restaurants);
        }
        return new Restaurants();
    }

    @Override
    public String update(Restaurants restaurants) {
        if(restaurants != null && restaurantsRepository.findById(restaurants.getId()).isPresent()){
            restaurantsRepository.save(restaurants);
            return "Restaurant successfully updated!";
        }
        return "Sorry, but the requested restaurant couldn't be found.";
    }

    @Override
    public List<Restaurants> getAllResults() throws MongoException {
        return restaurantsRepository.findAll();
    }

    @Override
    public Optional<Restaurants> searchById(String id) throws MongoException {
        return restaurantsRepository.findById(id);
    }

    @Override
    public boolean delete(String id) throws MongoException {
        if(restaurantsRepository.findById(id).isPresent()){
            restaurantsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsRestaurantByName(String name){
        return restaurantsRepository.existsByName(name);
    }

    public boolean existsRestaurantById(String id){
        return restaurantsRepository.existsById(id);
    }
}
