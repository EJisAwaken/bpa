import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from './confURL/environment';
import { Commande } from '../models/commande';

@Injectable({
  providedIn: 'root'
})
export class CommandeService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}


    getNiveau1(dir:string,critere: string,page: number, size: number) : Observable<any>{
        let params = new HttpParams()
        .append('page', page.toString())
        .append('size', size.toString());
        return this.http.get<any>(`${this.apiUrl}/commande?dir=${dir}&critere=${critere}`,{ params, ...this.getHttpOptions() });
    }
    
    getForLecteur(critere: string,validateur: string,etat: number,page: number, size: number) : Observable<any>{
        let params = new HttpParams()
        .append('page', page.toString())
        .append('size', size.toString());
        return this.http.get<any>(`${this.apiUrl}/commande/lecteur?critere=${critere}&validateur=${validateur}&etat=${etat}`,{ params, ...this.getHttpOptions() });
    }
    
    getByUidIdx3(uid:string , id_x3:string) : Observable<Commande>{
        return this.http.get<Commande>(`${this.apiUrl}/commande/lecteur-detail?uid=${uid}&id_x3=${id_x3}`,this.getHttpOptions());
    }

    validation(uid: string,id_x3: string,designation:string,valeur1:number,idetat:number,motif: string) : Observable<void>{
      return this.http.put<void>(`${this.apiUrl}/commande/valider?uid=${uid}&id_x3=${id_x3}&motif=${motif}&valeur1=${valeur1}&idetat=${idetat}&designation=${designation}`,null,this.getHttpOptions());
    }

    findByUid(uid: string,id_x3:string): Observable<Commande[]>{
      return this.http.get<Commande[]>(`${this.apiUrl}/commande/byUid?uid=${uid}&id_x3=${id_x3}`,this.getHttpOptions());
    }

    update(): Observable<string> {
      return this.http.put(`${this.apiUrl}/commande/update`, null, {
        ...this.getHttpOptions(),
        responseType: 'text'
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