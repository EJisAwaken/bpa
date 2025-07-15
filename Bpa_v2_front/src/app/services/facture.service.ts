import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from './confURL/environment';
import { Facture } from '../models/facture';

@Injectable({
  providedIn: 'root'
})
export class FactureService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    getNiveau1(critere: string,page: number, size: number) : Observable<any>{
        let params = new HttpParams()
        .append('page', page.toString())
        .append('size', size.toString());
        return this.http.get<any>(`${this.apiUrl}/facture?critere=${critere}`,{ params, ...this.getHttpOptions() });
    }

    getByUid(uid : string): Observable<Facture>{
        return this.http.get<Facture>(`${this.apiUrl}/facture/byUid?uid=${uid}`,this.getHttpOptions());
    }

    update(): Observable<string> {
      return this.http.put(`${this.apiUrl}/facture/update`, null, {
        ...this.getHttpOptions(),
        responseType: 'text'  // Préciser que la réponse est du texte
      });
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