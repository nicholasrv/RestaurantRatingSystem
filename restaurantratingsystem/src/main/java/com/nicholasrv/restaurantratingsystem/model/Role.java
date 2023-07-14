package com.nicholasrv.restaurantratingsystem.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@NoArgsConstructor

@Data
@Document(collection = "roles")
public class Role {
    @Id
    public String id;

    private String name;

    public Role(String name) {
        this.name = name;
    }
}
