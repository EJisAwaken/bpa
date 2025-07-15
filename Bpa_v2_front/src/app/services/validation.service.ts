import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Validation } from '../models/validation';
import { environment } from './confURL/environment';

@Injectable({
  providedIn: 'root'
})
export class ValidationService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    getAll(page: number,date1: string,date2: string,idUser:number): Observable<Validation[]>{
        return this.http.get<Validation[]>(`${this.apiUrl}/validation/all?page=${page}&date1=${date1}&date2=${date2}&idUser=${idUser}`,this.getHttpOptions())
    }
    
    getValidationSelected(uid: string,idUser: number): Observable<Validation>{
        return this.http.get<Validation>(`${this.apiUrl}/validation?uid=${uid}&idUser=${idUser}`,this.getHttpOptions())
    }
    
    getByUser(page: number,idUser: number,date1: string,date2: string): Observable<Validation[]>{
        return this.http.get<Validation[]>(`${this.apiUrl}/validation/byUser?page=${page}&idUser=${idUser}&date1=${date1}&date2=${date2}`,this.getHttpOptions())
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