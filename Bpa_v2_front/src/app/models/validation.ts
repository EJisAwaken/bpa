import { UserRequest } from "../object/userRequest";
import { Demande } from "./demande";
import { Etat } from "./etat";

export interface Validation{
    idValidation: number;
    etat: Etat;
    demande: string;
    user: UserRequest;
    motif: string;
    date_validation: string;
    objet: string;
    montant: string;
}