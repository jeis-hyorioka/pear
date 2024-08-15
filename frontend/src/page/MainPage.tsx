import axios from "axios";
import { useState } from "react";

export default function MainPage(){
  const baseUrl: string = "http://localhost:8080"
    interface Player {
        id?: string;
        name: string;
        isAssigned: boolean;
      }
      
      interface Room {
        id?: string;
        name: string;
        assignedPlayers: Player[];
      }

      const [players, setPlayers] = useState<Player[]>([]);
      const [rooms, setRooms] = useState<Room[]>([]);

        // プレイヤー追加
  const addPlayer = (nickname: string) => {
    axios.post(baseUrl + '/api/players', { nickname }).then(response => {
      setPlayers(response.data);
    });
  };

  // Room作成
  const addRoom = (roomName: string) => {
    axios.post(baseUrl + '/api/rooms', { roomName }).then(response => {
      setRooms(response.data);
    });
  };

  // Shuffleボタン押下
  const shufflePlayers = () => {
    axios.post(baseUrl + '/api/shuffle').then(response => {
      setRooms(response.data);
    });
  };

      return <>
        <h1>Pear</h1>
        <div>
        <h2>Players</h2>
        {players.map((player, i) => (
            <p key={player.id}>{player.name}</p>
        ))}
                <button onClick={() => addPlayer(prompt('Enter Player Nickname:') || '')}>
          プレイヤー追加
        </button>
        </div>
        <div>
        <h2>Rooms</h2>
        {rooms.map(room => (
          <div key={room.name}>
            <h3>{room.name}</h3>
            {room.assignedPlayers.map((room , index) => (
              <p key={room.id}>{room.name}</p>
            ))}
          </div>
        ))}
        <button onClick={() => addRoom(prompt('Enter Room Name:') || '')}>
          Room作成
        </button>
      </div>

      <button onClick={shufflePlayers}>Shuffle</button>
      </>
}