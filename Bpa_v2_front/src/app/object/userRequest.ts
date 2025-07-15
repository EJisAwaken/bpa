import { Role } from "../models/role";

export interface UserRequest{
    idUser: number;
    nom: string;
    prenom: string;
    email: string;
    id_x3: string;
    interim: string;
    role: Role;
    mot_de_passe: string;
    active:boolean;
}