package com.nicholasrv.restaurantratingsystem.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@Document(collection = "ratings")
public class Ratings {

    @Id
    private String id;
    
    private int score;
    
    private String comment;
    
    private LocalDateTime timestamp;
    
    @DBRef
    private UserEntity user;

}
