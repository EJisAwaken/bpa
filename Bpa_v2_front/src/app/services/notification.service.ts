import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from './confURL/environment';
import { Notification } from '../models/notification';

@Injectable({
  providedIn: 'root'
})
export class NotificationService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    getNotification(dir: string): Observable<Notification[]>{
        return this.http.get<Notification[]>(`${this.apiUrl}/notification?id_x3=${dir}`,this.getHttpOptions());
    }
    
    getNotificationForPD(dir: string): Observable<Notification[]>{
        return this.http.get<Notification[]>(`${this.apiUrl}/notification/valid2?dir=${dir}`,this.getHttpOptions());
    }
    
    getNotificationForDF(dir: string): Observable<Notification[]>{
        return this.http.get<Notification[]>(`${this.apiUrl}/notification/valid3?dir=${dir}`,this.getHttpOptions());
    }

    putZero(dir: String) : Observable<void>{
        return this.http.put<void>(`${this.apiUrl}/notification/putZero/${dir}`,null,this.getHttpOptions());
    }
    
    openNotif(uid: string,dir: string) : Observable<void>{
        return this.http.put<void>(`${this.apiUrl}/notification/ouvrir?uid=${uid}&dir=${dir}`,null,this.getHttpOptions());
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