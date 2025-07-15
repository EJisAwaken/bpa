import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Role } from '../models/role';
import { environment } from './confURL/environment';

@Injectable({
  providedIn: 'root'
})
export class RoleService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    public listRoles():Observable<Role[]>{
        return this.http.get<Role[]>(`${this.apiUrl}/role`,this.getHttpOptions());
    }

    findById(id: number): Observable<Role>{
      return this.http.get<Role>(`${this.apiUrl}/role/${id}`,this.getHttpOptions());
    }

    saveRole(role : Role): Observable<void>{
        return this.http.post<void>(`${this.apiUrl}/role`,role,this.getHttpOptions());
    }

    deleteRole(idRole : number) : Observable<void>{
        return this.http.put<void>(`${this.apiUrl}/role/${idRole}`,null,this.getHttpOptions());
    }

    updateRole(role : Role): Observable<void>{
      return this.http.put<void>(`${this.apiUrl}/role`,role,this.getHttpOptions());
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