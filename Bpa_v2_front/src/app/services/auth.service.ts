import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from './confURL/environment';
import { IsLogged } from '../models/isLogged';
import { WebSocketService } from './websocket.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl;
  private isLoggedInSubject: BehaviorSubject<boolean>;

  constructor(private http: HttpClient, private router: Router) {
    this.isLoggedInSubject = new BehaviorSubject<boolean>(this.isLoggedIn());
  }

  login(user: any): Observable<any> {
    return this.http.post<IsLogged>(`${this.apiUrl}/auth/login`, user).pipe(
      tap((response: IsLogged) => {
        this.saveToken(response.token); 
        this.isLoggedInSubject.next(true);
      })
    );
  }  

  saveToken(token: string) {
    localStorage.setItem('jwtToken', token);
  }

  getToken() {
    return localStorage.getItem('jwtToken');
  }

  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }

  getIsLoggedIn(): Observable<boolean> {
    return this.isLoggedInSubject.asObservable();
  }

  logout() {
    localStorage.removeItem('jwtToken');
    this.isLoggedInSubject.next(false);
    this.router.navigate(['/login']);
  }
}
