package com.nicholasrv.restaurantratingsystem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {
    private String restaurantId;
    private String name;
    private String location;
    private String cuisine;
}
