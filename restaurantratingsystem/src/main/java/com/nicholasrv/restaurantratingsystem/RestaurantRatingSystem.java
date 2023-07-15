package com.nicholasrv.restaurantratingsystem;

import com.mongodb.MongoException;

import java.util.List;
import java.util.Optional;

public interface RestaurantRatingSystem<T>{
    public T save (T t);

    public String update (T t);

    public List<T> getAllResults() throws MongoException;

    public Optional<T> searchById(String id) throws MongoException;

    public boolean delete(String id) throws MongoException;
}
