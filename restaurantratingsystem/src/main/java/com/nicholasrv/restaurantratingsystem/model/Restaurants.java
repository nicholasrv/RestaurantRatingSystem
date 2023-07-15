package com.nicholasrv.restaurantratingsystem.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Document(collection = "restaurants")
public class Restaurants {

    @Id
    private String id;

    private String name;

    private String location;

    private String cuisine;

    public Restaurants(String name, String location, String cuisine) {
        this.name = name;
        this.location = location;
        this.cuisine = cuisine;
    }
}
