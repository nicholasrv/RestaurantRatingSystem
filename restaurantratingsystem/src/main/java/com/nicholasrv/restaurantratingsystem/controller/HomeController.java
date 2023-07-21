package com.nicholasrv.restaurantratingsystem.controller;

import com.nicholasrv.restaurantratingsystem.model.Restaurants;
import com.nicholasrv.restaurantratingsystem.service.RestaurantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private RestaurantServiceImpl restaurantService;

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Restaurants> restaurants = restaurantService.getAllResults();
        model.addAttribute("restaurants", restaurants);
        return "home";
    }
}
