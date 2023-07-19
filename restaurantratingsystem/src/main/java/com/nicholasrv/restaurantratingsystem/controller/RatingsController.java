package com.nicholasrv.restaurantratingsystem.controller;

import com.nicholasrv.restaurantratingsystem.dto.RatingsDTO;
import com.nicholasrv.restaurantratingsystem.exceptions.BadRequestException;
import com.nicholasrv.restaurantratingsystem.model.Ratings;
import com.nicholasrv.restaurantratingsystem.model.Restaurants;
import com.nicholasrv.restaurantratingsystem.model.UserEntity;
import com.nicholasrv.restaurantratingsystem.repository.UserRepository;
import com.nicholasrv.restaurantratingsystem.service.RatingsServiceImpl;
import com.nicholasrv.restaurantratingsystem.service.RestaurantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
public class RatingsController {

    @Autowired
    RatingsServiceImpl ratingsService;

    @Autowired
    RestaurantServiceImpl restaurantService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getAllRatings() throws BadRequestException {
        List<Ratings> ratings = ratingsService.getAllResults();
        try {
            if (!ratings.isEmpty()) {
                return new ResponseEntity<List<Ratings>>(ratings, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No ratings could be found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("An internal error has occurred  while trying to retrieve the ratings. Please contact our support team for further information.");
        }
    }

    @GetMapping("/{restaurantId}/ratings")
    public ResponseEntity<?> getRatingsByRestaurant(@PathVariable String restaurantId) {

        ///checks if the requested restaurant exists
        boolean doesThisRestaurantExist = restaurantService.existsRestaurantById(restaurantId);

        // if the restaurant exists, searches for it + gets its id and returns all its related ratings
        if (doesThisRestaurantExist) {

            Restaurants restaurant = restaurantService.searchById(restaurantId).orElse(null);

            // If the search returns null returns the 404 response status
            if (restaurant == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<Ratings> ratings = ratingsService.findRatingsByRestaurant(restaurant);
            return new ResponseEntity<>(ratings, HttpStatus.OK);

        }

        //if the restaurant doesn't exist, cut the request by returning a bad request response.
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/{restaurantId}/ratings")
    public ResponseEntity<?> postRating(@PathVariable String restaurantId, @RequestBody RatingsDTO ratingsDTO) {

        // Gets both the user and restaurant
        UserEntity user = userRepository.findById(ratingsDTO.getUserId()).orElse(null);
        Restaurants restaurant = restaurantService.searchById(ratingsDTO.getRestaurantId()).orElse(null);
        if (restaurant == null) {
            return new ResponseEntity<>("Restaurant not found on the database", HttpStatus.NOT_FOUND);
        }

        //Creates a new Rating instance and sets all it's attributes
        Ratings newRating = new Ratings();

        newRating.setScore(ratingsDTO.getScore());
        newRating.setComment(ratingsDTO.getComment());
        newRating.setUser(user);
        newRating.setTimestamp(LocalDateTime.now());

        restaurant.getRatings().add(newRating);

        // saves the rating on the database + updates the restaurant object with the incremented list
        ratingsService.save(newRating);
        restaurantService.save(restaurant);

        return new ResponseEntity<>("Rating successfully created!", HttpStatus.CREATED);
    }

    @PutMapping("{id}/update")
    public ResponseEntity updateRating(@PathVariable("id") String id, @RequestBody RatingsDTO ratingsDTO) throws BadRequestException {
        try {
            Optional<Ratings> idRating = ratingsService.searchById(id);
            if (idRating.isPresent()) {
                Ratings ratingToBeUpdated = idRating.get();
                ratingToBeUpdated.setComment(ratingsDTO.getComment() != null ? ratingsDTO.getComment() : ratingToBeUpdated.getComment());
                ratingToBeUpdated.setScore(ratingsDTO.getScore() != 0 ? ratingsDTO.getScore() : ratingToBeUpdated.getScore());
                ratingToBeUpdated.setTimestamp(LocalDateTime.now());
                ratingsService.update(ratingToBeUpdated);
                return new ResponseEntity<>(ratingToBeUpdated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No rating was found with id " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("An internal error has occurred while trying to retrieve this rating. Please contact our support team for further information.");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteRatingById(@PathVariable("id") String id) throws BadRequestException {
        try {
            boolean haveItDeleted = ratingsService.delete(id);
            if (haveItDeleted) {
                return new ResponseEntity<>("The rating with id " + id + "has been successfully deleted from the database.", HttpStatus.OK);
            }
            return new ResponseEntity<>("The rating with id" + id + "hasn't been found on the database.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new BadRequestException("An internal error has occurred while trying to delete this rating. Please contact our support team for further information.");
        }
    }

    @GetMapping("/top-rated-restaurants")
    public ResponseEntity<List<Restaurants>> getTopRatedRestaurants() {
        List<Restaurants> restaurants = restaurantService.getAllResults();

        restaurants.sort((r1, r2) -> Double.compare(ratingsService.calculateAverageRating(r2), ratingsService.calculateAverageRating(r1)));

        if (restaurants.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(restaurants, HttpStatus.OK);
        }
    }


}
