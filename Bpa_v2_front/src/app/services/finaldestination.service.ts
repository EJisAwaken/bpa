import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from './confURL/environment';
import { Finaldestination } from '../models/finaldestination';

@Injectable({
  providedIn: 'root'
})
export class FinaldestinationService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    getAll() : Observable<Finaldestination[]>{
        return this.http.get<Finaldestination[]>(`${this.apiUrl}/final`,this.getHttpOptions());
    }
    
    getById(id : number) : Observable<Finaldestination>{
        return this.http.get<Finaldestination>(`${this.apiUrl}/final/${id}`,this.getHttpOptions());
    }

    insert(finaldestination : Finaldestination) : Observable<void>{
        return this.http.post<void>(`${this.apiUrl}/final`,finaldestination,this.getHttpOptions());
    }

    update(finaldestination : Finaldestination) : Observable<void> {
        return this.http.put<void>(`${this.apiUrl}/final`,finaldestination,this.getHttpOptions());
    }

    delete(id : number) : Observable<void>{
        return this.http.put<void>(`${this.apiUrl}/final/${id}`,null,this.getHttpOptions());
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