package com.pair.hyorioka.pair.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pair.hyorioka.pair.model.Player;
import com.pair.hyorioka.pair.model.Room;
import com.pair.hyorioka.pair.request.PlayerRequest;
import com.pair.hyorioka.pair.request.RoomRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class PairingController {
    private List<Player> players = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();

    @PostMapping("/players")
    public ResponseEntity<List<Player>> addPlayer(@RequestBody PlayerRequest playerRequest) {
        players.add(new Player(playerRequest.name())); 
        System.err.println("Adding player: " + playerRequest.name());      
        return ResponseEntity.ok(players);
    }

    @PutMapping("/rooms/{roomId}/assign/{playerId}")
    public ResponseEntity<List<Room>> assignPlayer(@PathVariable("roomId") String roomId, @PathVariable("playerId") String playerId) {
        for (Player player : players) {
            if (player.getId().equals(playerId)) {
                unassignPlayerByPlayerId(player.getId());
                for (Room room : rooms) {
                    if (room.getId().equals(roomId)) {
                        room.getAssignedPlayers().add(player);
                        player.setAssigned(true);
                        break;
                    }
                }
                break;
            }
        }
        return ResponseEntity.ok(rooms);
    }

    @PutMapping("/rooms/unassign/{playerId}")
    public ResponseEntity<List<Room>> unassignPlayer(@PathVariable("playerId") String playerId) {
        for (Player player : players) {
            if (player.getId().equals(playerId)) {
                unassignPlayerByPlayerId(player.getId());
            }
        }
        return ResponseEntity.ok(rooms);
    }
    
    @PostMapping("/rooms")
    public ResponseEntity<List<Room>> addRoom(@RequestBody RoomRequest roomRequest) {
        System.out.println("Adding room: " + roomRequest.name());
        rooms.add(new Room(roomRequest.name()));
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/shuffle")
    public ResponseEntity<List<Room>> shufflePlayers() {
        List<Player> unassignedPlayers = players.stream()
                                                .filter(player -> !player.isAssigned())
                                                .collect(Collectors.toList());
        Collections.shuffle(unassignedPlayers);

        int totalPlayers = players.size();
        int roomCount = rooms.size();
        int playersPerRoom = totalPlayers / roomCount;

        for (Room room: rooms) {
            while (room.getAssignedPlayers().size() < playersPerRoom && !unassignedPlayers.isEmpty()) {
                Player player = unassignedPlayers.remove(0);
                room.getAssignedPlayers().add(player);
                player.setAssigned(true);
            }
        }

        int roomIndex = 0;
        while (!unassignedPlayers.isEmpty()) {
            Player player = unassignedPlayers.remove(0);
            rooms.get(roomIndex).getAssignedPlayers().add(player);
            player.setAssigned(true);
            roomIndex = (roomIndex + 1) % roomCount;
        }

        return ResponseEntity.ok(rooms);
    }

    private void unassignPlayerByPlayerId(String playerId) {
        for (Room room : this.rooms) {
            Optional<Player> player = room.findPlayerById(playerId);
            player.ifPresent(p -> {
            p.setAssigned(false);
            List<Player> assignedPlayers = room.getAssignedPlayers();
            assignedPlayers.remove(p);
        });
        }
    }
}
