package com.pear.hyorioka.pear_art.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

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