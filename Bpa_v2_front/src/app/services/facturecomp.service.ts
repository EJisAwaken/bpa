import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from './confURL/environment';
import { Facture } from '../models/facture';
import { Facturecomp } from '../models/facturecomp';

@Injectable({
  providedIn: 'root'
})
export class FacturecompService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    getByMere(mere : string) : Observable<Facturecomp[]>{
        return this.http.get<Facturecomp[]>(`${this.apiUrl}/facture-comp?mere=${mere}`,this.getHttpOptions());
    }

    validation(uid:string,mere:string,id_x3:string,motif:string,valeur1: number,idetat:number): Observable<void>{
      return this.http.put<void>(`${this.apiUrl}/facture-comp/valider?uid=${uid}&mere=${mere}&id_x3=${id_x3}&motif=${motif}&valeur1=${valeur1}&idetat=${idetat}`,null,this.getHttpOptions());
    }

    update(): Observable<string> {
      return this.http.put(`${this.apiUrl}/facture-comp/update`, null, {
        ...this.getHttpOptions(),
        responseType: 'text'  // Préciser que la réponse est du texte
      });
    } 

    getByUid(uid : string): Observable<Facturecomp[]>{
      return this.http.get<Facturecomp[]>(`${this.apiUrl}/facture-comp/byUid?uid=${uid}`,this.getHttpOptions());
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