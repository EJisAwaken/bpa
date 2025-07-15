import { Etat } from "./etat";

export interface Demande{
    idDemande: number;
    uid: string;
    societe: string;
    demandeur : string;
    beneficiaire: string;
    date: string;
    id_x3: string;
    validateur: string;
    validation: string;
    montant: string;
    devise: string;
    codeA: string;
    lienPj:string;
    signature:string;
    isnotif:boolean;
    etat: Etat;
}