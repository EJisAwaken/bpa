import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { AuthService } from './auth.service';
import { User } from '../models/user';
import { UserRequest } from '../object/userRequest';
import { environment } from './confURL/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService{
    private apiUrl = environment.apiUrl;
    token:String | null=null;

    constructor(private http: HttpClient,private authService:AuthService) {}

    listUserActive(page: number, size: number,critere: string): Observable<any> {
        let params = new HttpParams()
        .append('page', page.toString())
        .append('size', size.toString());
        return this.http.get<UserRequest[]>(`${this.apiUrl}/user?critere=${critere}`,{ params, ...this.getHttpOptions() });
    }

    listAllUsers() : Observable<UserRequest[]>{
        return this.http.get<UserRequest[]>(`${this.apiUrl}/user/all`,this.getHttpOptions());
    }

    getUserSelected(id: number): Observable<UserRequest>{
        return this.http.get<UserRequest>(`${this.apiUrl}/user/${id}`,this.getHttpOptions());
    }
    
    getUserConnected(): Observable<UserRequest>{
        this.token = this.authService.getToken(); 
        return this.http.get<UserRequest>(`${this.apiUrl}/user/connected/${this.token}`, this.getHttpOptions());
    }
    
    saveNewUser(user : User):Observable<void>{
        return this.http.post<void>(`${this.apiUrl}/user`,user,this.getHttpOptions());
    } 
    
    saveDefaultUser(user : User):Observable<void>{
        return this.http.post<void>(`${this.apiUrl}/user/default`,user,this.getHttpOptions());
    } 

    changePwd(userId: number, oldPassword: string, newPassword: string): Observable<boolean> {
        const url = `${this.apiUrl}/user/changerMotDePasse/${userId}`;
        const params = { old: oldPassword, news: newPassword };
        
        return this.http.put<boolean>(url, null, { 
            params, 
            ...this.getHttpOptions() 
        });
    } 

    updateUser(user : User):Observable<any>{
        return this.http.put(`${this.apiUrl}/user`,user,this.getHttpOptions());
    }
    
    refreshPwd(id_x3: string,newPwd: string):Observable<any>{
        return this.http.put(`${this.apiUrl}/user/refreshPwd?id_x3=${id_x3}&newPwd=${newPwd}`,null,this.getHttpOptions());
    }
    
    deleteUser(id: number): Observable<any>{
        return this.http.put(`${this.apiUrl}/user/${id}`,null,this.getHttpOptions());
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