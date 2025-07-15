import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserRequest } from 'src/app/object/userRequest';
import { CommandeService } from 'src/app/services/commande.service';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-list-commande',
  templateUrl: './list-commande.component.html',
  styleUrls: ['./list-commande.component.css']
})
export class ListCommandeComponent {
  user: UserRequest | null = null;
  commandes: any[] =[] ;
  commandesIn: any[] =[] ;
  motif: string= '';
  critere: string= '';
  totalElements = 0;
  page = 0;
  size = 5;
  isLoading = false;
  isInterimView = false;

  constructor(
      private router: Router,
      private userService: UserService,
      private commandeService: CommandeService
    ) {}

    ngOnInit() {
      this.getUserConnected();
    }
  
    getUserConnected(): void {
      this.userService.getUserConnected().subscribe(
        (resultat: UserRequest) => {
          this.user = resultat;
          this.loadCommandes();
          this.loadCommandesIn();
          this.isLoading = false;
        },
        (error) => {
          console.error("Erreur lors de la récupération de l'utilisateur connecté :", error);
        }
      );
    }

    loadCommandes():void{
      if(this.user){
        this.commandeService.getNiveau1(this.user.id_x3,this.critere,this.page,this.size).subscribe({
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
    }
    
    loadCommandesIn():void{
      if(this.user){
        this.commandeService.getNiveau1(this.user.interim,this.critere,this.page,this.size).subscribe({
          next:(response)=>{
            this.commandesIn = response.content;
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
    }

    private handleError(message: string, error: any): void {
      console.error(message, error);
      Swal.fire('Erreur', message, 'error');
    }

    showRegularView() {
      this.isInterimView = false;
      this.loadCommandes();
    }
    
    showInterimView() {
      this.isInterimView = true;
      this.loadCommandesIn();
    } 

    onPageChange(event: any): void {
      this.page = event.page;
      this.size = event.rows;
      this.loadCommandes();
      this.loadCommandesIn();
    }

    onFormSubmitForDem(event: Event): void {
      event.preventDefault();
      this.loadCommandes();
      this.loadCommandesIn();
    }

    toReload(): void {
      this.critere = '';
      this.loadCommandes();
      this.loadCommandesIn();
    }
  
    redirectDetails(uid: string): void {
      this.router.navigate(['/details-bc', uid]);
    }
  
    ouvrirLien(lien : string): void {
      if(lien){
        window.open(lien , '_blank', 'noopener,noreferrer');
      }
    }
}
