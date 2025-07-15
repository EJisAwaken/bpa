import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Detail } from '../models/detail';
import { environment } from './confURL/environment';

@Injectable({
  providedIn: 'root'
})
export class DetailService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    getByUid(uid: string,id_x3:string):Observable<Detail[]>{
      return this.http.get<Detail[]>(`${this.apiUrl}/detail?uid=${uid}&id_x3=${id_x3}`,this.getHttpOptions());
    }

    validation(uid: string,ref_dem :string,code_article:string,id_x3: string,designation:string,valeur1:number,valeur2:number,valeur3:number,idetat:number,motif: string) : Observable<void>{
      return this.http.put<void>(`${this.apiUrl}/detail/valider?uid=${uid}&ref_dem=${ref_dem}&code_article=${code_article}&id_x3=${id_x3}&motif=${motif}&valeur1=${valeur1}&valeur2=${valeur2}&valeur3=${valeur3}&idetat=${idetat}&designation=${designation}`,null,this.getHttpOptions());
    }

    update(): Observable<string> {
      return this.http.put(`${this.apiUrl}/detail/update`, null, {
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