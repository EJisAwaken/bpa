import { Etat } from "./etat";

export interface Detail{
    idDetail: number;
    ref_dem: string;
    uid: string;
    code_article: string;
    quantite: string;
    pu: string;
    objet: string;
    montant: string;
    devise: string;
    fournisseur: string;
    id_x3: string;
    etat: Etat;
} 