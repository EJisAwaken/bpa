import { Etat } from "./etat";

export interface Commande{
    idDemande: number;
    uid: string;
    societe: string;
    demandeur : string;
    beneficiaire: string;
    date: string;
    id_x3: string;
    validateur: string;
    codepayement: string;
    modepayement: string;
    codeA: string;
    montant: string;
    devise: string;
    lienPj:string;
    signature:string;
    date_expiration: string;
    isnotif:boolean;
    etat: Etat;
}