package com.pear.hyorioka.pear_art.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pear.hyorioka.pear_art.model.Player;
import com.pear.hyorioka.pear_art.model.Room;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class PairingController {
    private List<Player> players = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();

    @PostMapping("/players")
    public ResponseEntity<List<Player>> addPlayer(@RequestBody String nickNamesString) {
        players.add(new Player(nickNamesString)) ;       
        return ResponseEntity.ok(players);
    }
    
    @PostMapping("/rooms")
    public ResponseEntity<List<Room>> addRoom(@RequestBody String roomNameString) {
        rooms.add(new Room(roomNameString));
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/shuffle")
    public ResponseEntity<List<Room>> shufflePlayers() {
        List<Player> unassignedPlayers = players.stream()
                                                .filter(player -> !player.isAssigned())
                                                .collect(Collectors.toList());
        int roomIndex = 0;
        for (Player player : unassignedPlayers) {
            rooms.get(roomIndex).getAssignedPlayers().add(player);
            player.setAssigned(true);
            roomIndex = (roomIndex + 1) % rooms.size();
        }
        return ResponseEntity.ok(rooms);
    }
    
    
}
