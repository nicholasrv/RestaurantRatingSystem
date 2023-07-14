package com.nicholasrv.restaurantratingsystem.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private String id;

    private String name;

    private String location;

    private String cuisine;
}