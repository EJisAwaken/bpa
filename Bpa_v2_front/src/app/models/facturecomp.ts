import { Etat } from "./etat";

export interface Facturecomp{
    idFacture: number;
    uid: string;
    mere: string;
    societe: string;
    fournisseur : string;
    objet : string;
    date: string;
    montant: string;
    devise: string;
    etat: Etat;
}