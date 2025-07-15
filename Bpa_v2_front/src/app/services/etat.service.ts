import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from './confURL/environment';
import { Facture } from '../models/facture';
import { Etat } from '../models/etat';

@Injectable({
  providedIn: 'root'
})
export class EtatService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    public getAll() : Observable<Etat[]>{
        return this.http.get<Etat[]>(`${this.apiUrl}/etat`,this.getHttpOptions()); 
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