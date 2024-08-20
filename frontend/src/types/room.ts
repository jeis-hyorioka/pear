import { Player } from "./player";

export type Room = {
    id: string;
    name: string;
    assignedPlayers: Player[];
    };