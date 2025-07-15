import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from './confURL/environment';
import { Menu } from '../models/menu';

@Injectable({
  providedIn: 'root'
})
export class MenuService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    getByIdRole(idRole : string) : Observable<Menu[]>{
        return this.http.get<Menu[]>(`${this.apiUrl}/menu?idRole=${idRole}`,this.getHttpOptions());
    }

    saveMenu(menu : Menu) : Observable<void>{
        return this.http.post<void>(`${this.apiUrl}/menu`,menu,this.getHttpOptions());
    }

    getById(idMenu : number) : Observable<Menu>{
      return this.http.get<Menu>(`${this.apiUrl}/menu/${idMenu}`,this.getHttpOptions());
    }

    deleteMenu(idMenu : number) : Observable<void>{
      return this.http.put<void>(`${this.apiUrl}/menu/${idMenu}`,null,this.getHttpOptions());
    }
    
    updateMenu(menu : Menu) : Observable<void>{
      return this.http.put<void>(`${this.apiUrl}/menu`,menu,this.getHttpOptions());
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