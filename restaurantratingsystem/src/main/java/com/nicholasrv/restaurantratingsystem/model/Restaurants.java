package com.nicholasrv.restaurantratingsystem.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "restaurants")
public class Restaurants {

    @Id
    private String id;

    private String name;

    private String location;

    private String cuisine;

    @DBRef
    private List<Ratings> ratings;

    public Restaurants() {
        this.ratings = new ArrayList<>();
    }

}
