package com.nicholasrv.restaurantratingsystem.controller;

import com.nicholasrv.restaurantratingsystem.exceptions.BadRequestException;
import com.nicholasrv.restaurantratingsystem.model.Ratings;
import com.nicholasrv.restaurantratingsystem.model.Restaurants;
import com.nicholasrv.restaurantratingsystem.service.RatingsServiceImpl;
import com.nicholasrv.restaurantratingsystem.service.RestaurantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingsController {
    @Autowired
    RatingsServiceImpl ratingsService;

    @Autowired
    RestaurantServiceImpl restaurantService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllRatings() throws BadRequestException {
        List<Ratings> ratings = ratingsService.getAllResults();
        try{
            if (!ratings.isEmpty()){
                return new ResponseEntity<List<Ratings>>(ratings, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No restaurants could be found.", HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new BadRequestException("An internal error has occurred  while trying to retrieve these restaurants. Please contact our support team for further information.");
        }
    }

    @GetMapping("/{restaurantId}/ratings")
    public ResponseEntity<?> getRatingsByRestaurant(@PathVariable String restaurantId) {

        ///checks if the requested restaurant exists
        boolean doesThisRestaurantExist = restaurantService.existsRestaurantById(restaurantId);

        // if the restaurant exists, searches for it + gets its id and returns all its related ratings
        if(doesThisRestaurantExist){

            Restaurants restaurant = restaurantService.searchById(restaurantId).orElse(null);

            // If the search returns null returns the 404 response status
            if(restaurant == null)  {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<Ratings> ratings = ratingsService.findRatingsByRestaurant(restaurant);
            return new ResponseEntity<>(ratings, HttpStatus.OK);

        }

        //if the restaurant doesn't exist, cut the request by returning a bad request response.
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

}
