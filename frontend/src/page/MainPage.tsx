import axios from "axios";
import { useState } from "react";
import { Player } from "../types/player";
import { Room } from "../types/room";
import PlayerPanel from "../component/playerPanel";

export default function MainPage(){
  const baseUrl: string = "http://localhost:8080"

      const [players, setPlayers] = useState<Player[]>([]);
      const [rooms, setRooms] = useState<Room[]>([]);

        // プレイヤー追加
  const addPlayer = (name: string) => {
    axios.post(baseUrl + '/api/players', { name }).then(response => {
      setPlayers(response.data);
    });
  };

  // Room作成
  const addRoom = (name: string) => {
    axios.post(baseUrl + '/api/rooms', { name }).then(response => {
      setRooms(response.data);
    });
  };

  const managePlayerAssignment = (roomId: string, playerId: string): void => {
    if(roomId === ""){
      unassignPlayer(playerId);
    }
    else{
      assignPlayer(roomId, playerId);
    }
  }

  const assignPlayer = (roomId: string, playerId: string): void => {
    axios.put(baseUrl + '/api/rooms/' + roomId + '/assign/' + playerId ).then(response => {
      setRooms(response.data);
    });
  }

  const unassignPlayer = (playerId: string): void => {
    axios.put(baseUrl + '/api/rooms/unassign/' + playerId).then(response => {
      setRooms(response.data);
    }) 
  }

  // Shuffleボタン押下
  const shufflePlayers = () => {
    axios.post(baseUrl + '/api/shuffle').then(response => {
      setRooms(response.data);
    });
  };

      return <>
        <h1 className="mb-4 text-5xl font-extrabold">Pear</h1>
        <div>
        <h2 className="mb-4 text-4xl font-extrabold">Players</h2>
        {players.map((player, i) => (
          <div>
            <p key={player.id}>{player.name}</p>
          </div>
        ))}
                <button onClick={() => addPlayer(prompt('Enter Player Nickname:') || '')}>
          プレイヤー追加
        </button>
        </div>
        <h2 className="mb-4 text-4xl font-extrabold">Rooms</h2>

        <div className="flex">
        {rooms.map(room => (
          <div key={room.id} className="mr-4 rounded bg-teal-300">
            <h3>{room.name}</h3>
            {room.assignedPlayers.map((player) => (
              <PlayerPanel player={player} rooms={rooms} selectedRoom={room} onChangeRoom={managePlayerAssignment}/>
            ))}
          </div>
        ))}

      </div>
      <button onClick={() => addRoom(prompt('Enter Room Name:') || '')}>
          Room作成
        </button>

      <button onClick={shufflePlayers}>Shuffle</button>
      </>
}