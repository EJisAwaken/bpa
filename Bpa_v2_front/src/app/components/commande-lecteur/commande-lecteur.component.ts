import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Etat } from 'src/app/models/etat';
import { UserRequest } from 'src/app/object/userRequest';
import { CommandeService } from 'src/app/services/commande.service';
import { EtatService } from 'src/app/services/etat.service';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-commande-lecteur',
  templateUrl: './commande-lecteur.component.html',
  styleUrls: ['./commande-lecteur.component.css']
})
export class CommandeLecteurComponent {
    commandes: any[] =[] ;
    users : UserRequest [] =[];
    etats : Etat[]=[];
    motif: string= '';
    critere: string= '';
    validateur: string= '';
    etat=0;
    totalElements = 0;
    page = 0;
    size = 5;
    isLoading = false;
  
    constructor(
        private router: Router,
        private userService: UserService,
        private etatService: EtatService,
        private commandeService: CommandeService
      ) {}
  
      ngOnInit() {
        this.loadCommandes();
        this.getAllUsers();
        this.getAllEtat();
      }
  
      loadCommandes():void{
        this.commandeService.getForLecteur(this.critere,this.validateur,this.etat,this.page,this.size).subscribe({
          next:(response)=>{
            this.commandes = response.content;
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
        this.size = event.rows;
        this.loadCommandes();
      }
  
      onFormSubmitForDem(event: Event): void {
        event.preventDefault();
        this.loadCommandes();
      }
  
      toReload(): void {
        this.critere = '';
        this.validateur = '';
        this.etat = 0;
        this.loadCommandes();
      }
    
      redirectDetails(uid: string,id_x3:string): void {
        this.router.navigate(['/details-bc-lecteur', uid,id_x3]);
      }

      redirectDA(): void {
        this.router.navigate(['lec-da']);
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
