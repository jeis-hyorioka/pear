package com.pair.hyorioka.pair.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Player {
    private String id = UUID.randomUUID().toString();
    private String name;
    private boolean isAssigned = false;

   public Player(String name){
        this.name = name;
    }
}
