import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from './confURL/environment';
import { Detailmere } from '../models/detailmere';

@Injectable({
  providedIn: 'root'
})
export class DetailmereService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    getByMere(mere: string) : Observable<Detailmere[]>{
        return this.http.get<Detailmere[]>(`${this.apiUrl}/detail-mere?mere=${mere}`,this.getHttpOptions());
    }

    update(): Observable<string> {
      return this.http.put(`${this.apiUrl}/detail-mere/update`, null, {
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