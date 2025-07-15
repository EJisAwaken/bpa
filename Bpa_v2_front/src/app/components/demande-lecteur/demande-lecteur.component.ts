import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Etat } from 'src/app/models/etat';
import { UserRequest } from 'src/app/object/userRequest';
import { DemandeService } from 'src/app/services/demande.service';
import { EtatService } from 'src/app/services/etat.service';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-demande-lecteur',
  templateUrl: './demande-lecteur.component.html',
  styleUrls: ['./demande-lecteur.component.css']
})
export class DemandeLecteurComponent {
    demandes: any[] = [];
    users : UserRequest [] =[];
    etats : Etat[] =[];
    motif: string= '';
    critere: string= '';
    validateur: string= '';
    etat= 0;
    totalElements = 0;
    page = 0;
    size = 5;
    isLoading = false;
  
    constructor(
      private router: Router,
      private userService: UserService,
      private etatService: EtatService,
      private demandeService: DemandeService
    ) {}
  
    ngOnInit() {
      this.loadDemandes();
      this.getAllUsers();
      this.getAllEtat();
    }
  
    loadDemandes() : void {
      this.demandeService.getForLecteur(this.critere,this.validateur,this.etat,this.page,this.size).subscribe({
        next: (response) => {
          this.demandes = response.content;
          this.totalElements = response.totalElements;
        },
        error: (error) => {
          this.handleError('Votre session est expirée ou une erreur est survenue lors du chargement des données', error);
        },
        complete: () => {
          this.isLoading = false;
        }
      })
    } 
    
    getAllUsers(){
      this.userService.listAllUsers().subscribe(
        (data : UserRequest[])=>{
          this.users = data;
          this.isLoading =false;
        }
      )
    }

    getAllEtat() {
      this.etatService.getAll().subscribe(
        (data: Etat[]) => {
          this.isLoading =false;
          this.etats = data;
        }
      )
    }
  
    private handleError(message: string, error: any): void {
      console.error(message, error);
      Swal.fire('Erreur', message, 'error');
    }     
  
    onPageChange(event: any): void {
      this.page = event.page;
      this.size = parseInt(event.rows);
      this.loadDemandes();
    }
    
    onFormSubmitForDem(event: Event): void {
      event.preventDefault();
      this.loadDemandes();
    }
    
    toReload(): void {
      this.critere = '';
      this.validateur = '';
      this.etat =0;
      this.loadDemandes();
    }
  
    redirectDetails(uid: string,id_x3:string): void {
      this.router.navigate(['details-da-lecteur', uid,id_x3]);
    }
    redirectBC(): void {
      this.router.navigate(['lec-bc']);
    }
    
    redirectFC(): void {
      this.router.navigate(['lec-fc']);
    }
    
    ouvrirLien(lien : string): void {
      if(lien){
        window.open(lien , '_blank', 'noopener,noreferrer');
      }
    }
}
