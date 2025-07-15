import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Etat } from 'src/app/models/etat';
import { EtatService } from 'src/app/services/etat.service';
import { FacturecompettService } from 'src/app/services/facturecompett.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-facture-lecteur',
  templateUrl: './facture-lecteur.component.html',
  styleUrls: ['./facture-lecteur.component.css']
})
export class FactureLecteurComponent {
    critere: string= '';
    etat=0;
    factures : any [] =[];
    etats : Etat[] =[];
    totalElements = 0;
    page = 0;
    size = 5;
    isLoading = false;
  
    constructor(
      private router: Router,
      private facturecompettService: FacturecompettService,
      private etatService: EtatService
    ) {}
  
    ngOnInit() {
      this.getAllPage();
      this.getAllEtat();
    }
  
    getAllPage() : void{
      this.facturecompettService.getForlecteur(this.critere,this.etat,this.page,this.size).subscribe({
        next: (response) => {
          this.factures = response.content;
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
  
    private handleError(message: string, error: any): void {
      console.error(message, error);
      Swal.fire('Erreur', message, 'error');
    }
  
  
    onPageChange(event: any): void {
      this.page = event.page;
      this.size = event.rows;
      this.getAllPage();
    }
  
    onFormSubmitForDem(event: Event): void {
      event.preventDefault();
      this.getAllPage();
    }

    getAllEtat() {
      this.etatService.getAll().subscribe(
        (data: Etat[]) => {
          this.isLoading =false;
          this.etats = data;
        }
      )
    }
  
    toReload(): void {
      this.critere = '';
      this.etat =0;
      this.getAllPage();
    }
  
    redirectDetails(uid: string): void {
      this.router.navigate(['/details-fc-lecteur', uid]);
    }

    redirectBC(): void {
      this.router.navigate(['lec-bc']);
    }
    redirectDA(): void {
      this.router.navigate(['lec-da']);
    }
  
    ouvrirLien(lien : string): void {
      if(lien){
        window.open(lien , '_blank', 'noopener,noreferrer');
      }
    }
}
