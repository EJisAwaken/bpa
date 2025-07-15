import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from './confURL/environment';
import { DataAttente} from '../models/dataAttente';
import { Charts } from '../models/charts';
import { DataAdmin } from '../models/dataAdmin';

@Injectable({
  providedIn: 'root'
})
export class StatistiqueService{
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient){}

    getDataVal(date1:string,date2:string,idUser: number):Observable<DataAdmin>{
        return this.http.get<DataAdmin>(`${this.apiUrl}/stat/user?date1=${date1}&date2=${date2}&idUser=${idUser}`,this.getHttpOptions());
    }
    
    getDataAdmin(date1:string,date2:string):Observable<DataAdmin>{
        return this.http.get<DataAdmin>(`${this.apiUrl}/stat/admin?date1=${date1}&date2=${date2}`,this.getHttpOptions());
    }
    
    getDataAttente():Observable<DataAttente>{
        return this.http.get<DataAttente>(`${this.apiUrl}/stat/admin-attente`,this.getHttpOptions());
    }
    
    getDataAttenteVal(idUser:string):Observable<DataAttente>{
        return this.http.get<DataAttente>(`${this.apiUrl}/stat/val-attente?idUser=${idUser}`,this.getHttpOptions());
    }

    getDataChartUser(date1:string,date2:string,idUser: number):Observable<Charts[]>{
        return this.http.get<Charts[]>(`${this.apiUrl}/stat/chartuser?date1=${date1}&date2=${date2}&idUser=${idUser}`,this.getHttpOptions());
    }
    
    getDataChartAdmin(date1:string,date2:string):Observable<Charts[]>{
        return this.http.get<Charts[]>(`${this.apiUrl}/stat/chartadmin?date1=${date1}&date2=${date2}`,this.getHttpOptions());
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