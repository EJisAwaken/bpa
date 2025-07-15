import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Detailmere } from 'src/app/models/detailmere';
import { DetailmereService } from 'src/app/services/detailmere.service';
import { FacturecompettService } from 'src/app/services/facturecompett.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-facture',
  templateUrl: './facture.component.html',
  styleUrls: ['./facture.component.css']
})
export class FactureComponent {
  critere: string= '';
  factures : any [] =[];
  uid = 'FAE2412TAN00004';
  nbr = 0 ;
  totalElements = 0;
  page = 0;
  size = 5;
  isLoading = false;

  constructor(
    private router: Router,
    private facturecompettService: FacturecompettService,
    private detailmereService : DetailmereService
  ) {}

  ngOnInit() {
    this.getAllPage();
  }

  getAllPage() : void{
    this.facturecompettService.getNiveau1(this.critere,this.page,this.size).subscribe({
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

  toReload(): void {
    this.critere = '';
    this.getAllPage();
  }

  redirectDetails(uid: string): void {
    this.router.navigate(['/details-fac', uid]);
  }

  ouvrirLien(lien : string): void {
    if(lien){
      window.open(lien , '_blank', 'noopener,noreferrer');
    }
  }
}
