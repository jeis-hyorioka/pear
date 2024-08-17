package com.pear.hyorioka.pear_art.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pear.hyorioka.pear_art.model.Player;
import com.pear.hyorioka.pear_art.model.Room;

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
    public ResponseEntity<List<Player>> addPlayer(@RequestBody String nickNamesString) {
        players.add(new Player(nickNamesString));       
        return ResponseEntity.ok(players);
    }

    @PutMapping("/rooms/{roomId}/assign/{playerId}")
    public ResponseEntity<List<Room>> assignPlayer(@PathVariable("roomId") String roomId, @PathVariable("playerId") String playerId) {
        System.out.println("roomId: " + roomId + ", playerId: " + playerId);
        for (Player player : players) {
            if (player.getId().equals(playerId)) {
                System.out.println("プレイヤー発見！　playerId: " + playerId);
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
        System.out.println("アサイン外すよ！！　playerId: " + playerId);
        for (Player player : players) {
            if (player.getId().equals(playerId)) {
                unassignPlayerByPlayerId(player.getId());
            }
        }
        System.out.println(rooms);
        return ResponseEntity.ok(rooms);
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
        Collections.shuffle(unassignedPlayers);
        int roomIndex = 0;
        for (Player player : unassignedPlayers) {
            rooms.get(roomIndex).getAssignedPlayers().add(player);
            player.setAssigned(true);
            roomIndex = (roomIndex + 1) % rooms.size();
        }
        return ResponseEntity.ok(rooms);
    }

    private void unassignPlayerByPlayerId(String playerId) {
        System.out.println("削除するplayerId :" + playerId);
        for (Room room : this.rooms) {
            Optional<Player> player = room.findPlayerById(playerId);
            System.out.println("playerいた？:" +player.isPresent());
            player.ifPresent(p -> {
            p.setAssigned(false);
            List<Player> assignedPlayers = room.getAssignedPlayers();
            System.out.println("動いてる？");
            System.out.println(assignedPlayers);
            assignedPlayers.remove(p);
            System.out.println(assignedPlayers);
        });
        }
        System.out.println("現在のRooms");
        System.out.println(this.rooms);
    }
}
