import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Demande } from '../models/demande';
import { environment } from './confURL/environment';

@Injectable({
  providedIn: 'root'
})
export class DemandeService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    getByUid(uid: string):Observable<Demande>{
      return this.http.get<Demande>(`${this.apiUrl}/demande/byUid?uid=${uid}`,this.getHttpOptions());
    }
    
    getByUidX3(uid: string,id_x3: string):Observable<Demande>{
      return this.http.get<Demande>(`${this.apiUrl}/demande/byUidX3?uid=${uid}&id_x3=${id_x3}`,this.getHttpOptions());
    }

    getNiveau1(dir:string,critere: string,page: number, size: number) : Observable<any>{
        let params = new HttpParams()
        .append('page', page.toString())
        .append('size', size.toString());
        return this.http.get<any>(`${this.apiUrl}/demande/niveau1?dir=${dir}&critere=${critere}`,{ params, ...this.getHttpOptions() });
    }
    
    getForLecteur(critere: string,validateur: string,etat: number,page: number, size: number) : Observable<any>{
        let params = new HttpParams()
        .append('page', page.toString())
        .append('size', size.toString());
        return this.http.get<any>(`${this.apiUrl}/demande/lecteur?critere=${critere}&validateur=${validateur}&etat=${etat}`,{ params, ...this.getHttpOptions() });
    }


    validation(uid: string,id_x3: string,designation:string,valeur1:number,valeur2:number,valeur3:number,idetat:number,motif: string) : Observable<void>{
        return this.http.put<void>(`${this.apiUrl}/demande/valider?uid=${uid}&id_x3=${id_x3}&motif=${motif}&valeur1=${valeur1}&valeur2=${valeur2}&valeur3=${valeur3}&idetat=${idetat}&designation=${designation}`,null,this.getHttpOptions());
    }

    update(): Observable<string> {
      return this.http.put(`${this.apiUrl}/demande/update`, null, {
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