import React from 'react';
import { Player } from '../types/player';
import { Room } from '../types/room';

interface PlayerPanelProps {
    player: Player;
    rooms: Room[];
    selectedRoom: Room
    onChangeRoom: (roomId: string, playerId: string) => void;
}

const PlayerPanel: React.FC<PlayerPanelProps> = ({player, rooms, selectedRoom, onChangeRoom}) => {
    const handleRoomChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        onChangeRoom(event.target.value, player.id);
    };

    console.log(player)

    return (
        <div className='flex flex-col m-4 max-w-sm p-6 bg-white border border-gray-200 rounded-lg shadow dark:bg-gray-800 dark:border-gray-700'>
            <label htmlFor="username" className='font-bold'>{player.name}</label>
            <select id="room" value={selectedRoom.id} onChange={handleRoomChange}>
            <option value="">no asigne</option>
                {rooms.map((room, index) => (
                    <option key={index} value={room.id}>{room.name}</option>
                ))
                }
            </select>
            
        </div>
    );
};

export default PlayerPanel;