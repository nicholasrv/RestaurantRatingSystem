package com.nicholasrv.restaurantratingsystem.controller;

import com.nicholasrv.restaurantratingsystem.dto.RatingsDTO;
import com.nicholasrv.restaurantratingsystem.dto.RestaurantDTO;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantServiceImpl restaurantService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RatingsServiceImpl ratingsService;

    @GetMapping("/restaurants/{id}")
    public String getRestaurantDetails(@PathVariable String id, Model model) {
        Optional<Restaurants> restaurant = restaurantService.searchById(id);
        if (restaurant.isPresent()) {
            model.addAttribute("restaurant", restaurant.get());
            return "restaurant-details";
        } else {
            return "error-page";
        }
    }

    @GetMapping("/restaurants/{id}/ratings")
    public String getRatingForm(@PathVariable String id, Model model) {
        RatingsDTO ratingsDTO = new RatingsDTO();
        ratingsDTO.setRestaurantId(id);
        model.addAttribute("ratingsDTO", ratingsDTO);
        return "rating-form";
    }

    @PostMapping("/restaurants/{id}/ratings")
    public String postRating(@PathVariable String id, @ModelAttribute RatingsDTO ratingsDTO, Model model) {
        Restaurants restaurant = restaurantService.searchById(ratingsDTO.getRestaurantId()).orElse(null);
        if (restaurant == null) {
            return "error-page";
        }

        UserEntity user = userRepository.findById(ratingsDTO.getUserId()).orElse(null);
        if (user == null) {
            return "error-page";
        }

        Ratings newRating = new Ratings();
        newRating.setScore(ratingsDTO.getScore());
        newRating.setComment(ratingsDTO.getComment());
        newRating.setUser(user);
        newRating.setTimestamp(LocalDateTime.now());

        restaurant.getRatings().add(newRating);
        ratingsService.save(newRating);
        restaurantService.save(restaurant);

        return "redirect:/restaurants/" + id;
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllRestaurants() throws BadRequestException {
        List<Restaurants> restaurants = restaurantService.getAllResults();
        try{
            if (!restaurants.isEmpty()){
                return new ResponseEntity<List<Restaurants>>(restaurants, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No restaurants could be found.", HttpStatus.NOT_FOUND);
            }
    } catch(Exception e) {
            e.printStackTrace();
            throw new BadRequestException("An internal error has occurred  while trying to retrieve these restaurants. Please contact our support team for further information.");
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> saveNewRestaurant(@RequestBody RestaurantDTO restaurantDTO) throws BadRequestException {
        try {
            Restaurants restaurants = new Restaurants();
            restaurants.setName(restaurantDTO.getName());
            restaurants.setLocation(restaurantDTO.getLocation());
            restaurants.setCuisine(restaurantDTO.getCuisine());

            boolean doesThisRestaurantExist = restaurantService.existsRestaurantByName(restaurants.getName());
            if (doesThisRestaurantExist){
                return ResponseEntity.badRequest().body("This restaurant is already registered on our database.");
            }
            return ResponseEntity.ok("Restaurant successfully saved!" + restaurantService.save(restaurants));
        } catch (Exception e){
            e.printStackTrace();
            throw new BadRequestException("An internal error has occurred while trying to save this restaurant. Please contact our support team for further information.");
        }
    }

    @PutMapping("{id}/update")
    public ResponseEntity updateRestaurantDetails(@PathVariable("id") String id, @RequestBody RestaurantDTO restaurantDTO) throws BadRequestException {
        try {
        Optional<Restaurants> idRestaurant = restaurantService.searchById(id);
        if (idRestaurant.isPresent()){
            Restaurants restaurantToBeUpdated = idRestaurant.get();
            restaurantToBeUpdated.setLocation(restaurantDTO.getLocation() != null ? restaurantDTO.getLocation() : restaurantToBeUpdated.getLocation());
            restaurantToBeUpdated.setLocation(restaurantDTO.getName() != null ? restaurantDTO.getName() : restaurantToBeUpdated.getName());
            restaurantToBeUpdated.setLocation(restaurantDTO.getCuisine() != null ? restaurantDTO.getCuisine() : restaurantToBeUpdated.getCuisine());
            restaurantService.update(restaurantToBeUpdated);
            return new ResponseEntity<>(restaurantToBeUpdated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No restaurant was found with id " + id, HttpStatus.NOT_FOUND);
        }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("An internal error has occurred  while trying to retrieve these restaurants. Please contact our support team for further information.");
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable("id") String id) {
        Optional<Restaurants> idRestaurant = restaurantService.searchById(id);
        if (idRestaurant.isPresent()) {
            return new ResponseEntity<>(idRestaurant.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No restaurant was found with id " + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteRestaurantById(@PathVariable("id") String id) throws BadRequestException {
        try{
            boolean haveItDeleted = restaurantService.delete(id);
            if(haveItDeleted){
                return new ResponseEntity<>("The restaurant with id " + id + "has been successfully deleted from the database.", HttpStatus.OK);
            }
            return new ResponseEntity<>("The restaurant with id" + id + "hasn't been found on the database.", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            throw new BadRequestException("An internal error has occurred  while trying to delete this restaurant. Please contact our support team for further information.");
        }
    }

}
