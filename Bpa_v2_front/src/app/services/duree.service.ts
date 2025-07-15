import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Duree } from '../models/duree';
import { environment } from './confURL/environment';

@Injectable({
  providedIn: 'root'
})
export class DureeService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    getDureeById(id: number) : Observable<Duree>{
      return this.http.get<Duree>(`${this.apiUrl}/duree/byId/${id}`,this.getHttpOptions());
    }

    getDureeForUser(idUser: number): Observable<Duree[]> {
      return this.http.get<Duree | null>(`${this.apiUrl}/duree?idUser=${idUser}`, this.getHttpOptions()).pipe(
        map(response => response ? [response] : []) // Convertit en tableau
      );
    }    

    getDureeByUser(page: number, size: number,id: number,date:string): Observable<any>{
        let params = new HttpParams()
        .append('page', page.toString())
        .append('size', size.toString());
        return this.http.get<Duree[]>(`${this.apiUrl}/duree/${id}?date=${date}`,{ params, ...this.getHttpOptions()})
    }

    saveDuree(duree : Duree): Observable<void>{
        return this.http.post<void>(`${this.apiUrl}/duree`,duree,this.getHttpOptions());
    }

    updateDuree(duree: Duree) : Observable<void>{
        return this.http.put<void>(`${this.apiUrl}/duree`,duree,this.getHttpOptions());
    }

    deleteDuree(id: number) : Observable<void>{
        return this.http.put<void>(`${this.apiUrl}/duree/desc/${id}`, null, this.getHttpOptions());
    }
    
    rebornDuree(id: number) : Observable<void>{
        return this.http.put<void>(`${this.apiUrl}/duree/asc/${id}`, null, this.getHttpOptions());
    }

    upgradeDuree(): Observable<void>{
        return this.http.put<void>(`${this.apiUrl}/duree/turnOff`,null,this.getHttpOptions());
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