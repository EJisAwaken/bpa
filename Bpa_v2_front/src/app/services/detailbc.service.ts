import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Detailbc } from '../models/detailbc';
import { environment } from './confURL/environment';

@Injectable({
  providedIn: 'root'
})
export class DetailbcService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    getByUid(uid: string):Observable<Detailbc[]>{
      return this.http.get<Detailbc[]>(`${this.apiUrl}/detailbc?uid=${uid}`,this.getHttpOptions());
    }   

    update(): Observable<string> {
      return this.http.put(`${this.apiUrl}/detailbc/update`, null, {
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