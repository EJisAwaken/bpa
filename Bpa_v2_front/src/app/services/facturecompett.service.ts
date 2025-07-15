import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from './confURL/environment';
import { Facturecompett } from '../models/facturecompett';

@Injectable({
  providedIn: 'root'
})
export class FacturecompettService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    getNiveau1(critere: string,page: number, size: number) : Observable<any>{
        let params = new HttpParams()
        .append('page', page.toString())
        .append('size', size.toString());
        return this.http.get<any>(`${this.apiUrl}/facture-comp-ett?critere=${critere}`,{ params, ...this.getHttpOptions() });
    }

    getForlecteur(critere: string,etat: number,page: number, size: number) : Observable<any>{
        let params = new HttpParams()
        .append('page', page.toString())
        .append('size', size.toString());
        return this.http.get<any>(`${this.apiUrl}/facture-comp-ett/lecteur?critere=${critere}&etat=${etat}`,{ params, ...this.getHttpOptions() });
    }

    getByUid(uid : string): Observable<Facturecompett>{
          return this.http.get<Facturecompett>(`${this.apiUrl}/facture-comp-ett/byUid?uid=${uid}`,this.getHttpOptions());
    }

    validation(uid:string,id_x3:string,motif:string,valeur1: number,idetat:number): Observable<void>{
        return this.http.put<void>(`${this.apiUrl}/facture-comp-ett/valider?uid=${uid}&id_x3=${id_x3}&motif=${motif}&valeur1=${valeur1}&idetat=${idetat}`,null,this.getHttpOptions());
      }

    private getHttpOptions() {
        const token = localStorage.getItem('jwtToken');
        return {
          headers: new HttpHeaders({
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          })
        };
    }
}