import { Etat } from "./etat";

export interface Facture{
    idFacture: number;
    uid: string;
    societe: string;
    fournisseur : string;
    date: string;
    montant: string;
    devise: string;
    etat: Etat;
}