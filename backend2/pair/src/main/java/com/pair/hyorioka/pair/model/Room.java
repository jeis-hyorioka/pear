package com.pair.hyorioka.pair.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Room {
    private String id = UUID.randomUUID().toString();
    private String name;
    private List<Player> assignedPlayers = new ArrayList<>();

    public Room(String name) {
        this.name = name;
    }

    public Optional<Player> findPlayerById(String playerId) {
        for(Player player : assignedPlayers) {
            if (player.getId().equals(playerId)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }
}
