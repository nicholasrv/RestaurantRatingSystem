package com.nicholasrv.restaurantratingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingsDTO {
    private String ratingId;
    private int score;
    private String comment;
    private LocalDateTime timestamp;
    private String userId;
    private String restaurantId;

}
