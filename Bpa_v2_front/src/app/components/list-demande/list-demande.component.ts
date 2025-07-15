import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserRequest } from 'src/app/object/userRequest';
import { DemandeService } from 'src/app/services/demande.service';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-list-demande',
  templateUrl: './list-demande.component.html',
  styleUrls: ['./list-demande.component.css']
})
export class ListDemandeComponent {
  user: UserRequest | null = null;
  demandes: any[] = [];
  demandesIn: any[] = [];
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
    private demandeService: DemandeService
  ) {}

  ngOnInit() {
    this.getUserConnected();
  }

  getUserConnected(): void {
    this.userService.getUserConnected().subscribe(
      (resultat: UserRequest) => {
        this.user = resultat;
        this.loadDemandes();
        this.isLoading = false;
      },
      (error) => {
        console.error("Erreur lors de la récupération de l'utilisateur connecté :", error);
      }
    );
  }

  loadDemandesForIn() : void {
    if(this.user && this.user.interim){
        this.demandeService.getNiveau1(this.user.interim,this.critere,this.page,this.size).subscribe({
          next: (response) => {
            this.demandesIn = response.content;
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

  loadDemandes() : void {
    if(this.user){
        this.demandeService.getNiveau1(this.user.id_x3,this.critere,this.page,this.size).subscribe({
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
  }

  toValid(uid: string): void {
    Swal.fire({
      title: 'Confirmer la validation',
      text: "Êtes-vous sûr de valider cette demande ?",
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Valider',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed && this.user) {
        this.isLoading = true;
        const valeur1 = 3;
        const valeur2 = 3;
        const valeur3 = 5;
        const idetat = 3;
        const designation = 'VAL';
        this.demandeService
          .validation(uid, this.user.id_x3, designation, valeur1, valeur2, valeur3,idetat,this.motif)
          .subscribe({
            next: () => {
              Swal.fire('Succès !', 'La demande a été validée avec succès.', 'success')
                .then(() => {
                  window.location.reload(); 
                });
              this.isLoading = false;
            },
            error: (error) => {
              this.handleError('Erreur lors de la validation', error);
              this.isLoading = false;
            }
          });
      }
    });
  }

  toRefuse(uid: string): void { 
    Swal.fire({
      title: 'Confirmer le refus',
      text: "Êtes-vous sûr de refuser cette demande ?",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Confirmer',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          title: 'Motif du refus',
          html:
            '<div class="form-group">' +
            '<textarea id="motif" class="form-control" rows="4" placeholder="Veuillez saisir le motif de refus..." required></textarea>' +
            '</div>',
          focusConfirm: false,
          showCancelButton: true,
          confirmButtonText: 'Envoyer',
          cancelButtonText: 'Annuler',
          preConfirm: () => {
            const motif = (document.getElementById('motif') as HTMLInputElement).value;
            if (!motif) {
              Swal.showValidationMessage('Veuillez saisir un motif de refus');
              return false;
            }
            return motif;
          }
        }).then((result) => {
          if (result.isConfirmed && this.user) {
            this.isLoading = true;
            this.motif = result.value;
            const valeur1 = 1;
            const valeur2 = 1;
            const valeur3 = 5;
            const idetat = 4;
            const designation = 'REJ';
      
            this.demandeService
              .validation(uid, this.user.id_x3, designation, valeur1, valeur2, valeur3,idetat,this.motif)
              .subscribe({
                next: () => {
                  Swal.fire('Succès !', 'La demande a été refusée avec succès.', 'success')
                    .then(() => {
                      window.location.reload(); 
                    });
                  this.isLoading = false;
                },
                error: (error) => {
                  this.handleError('Erreur lors de la refus', error);
                  this.isLoading = false;
                }
              });
          }
        });
      }
    });
  }
  

  private handleError(message: string, error: any): void {
    console.error(message, error);
    Swal.fire('Erreur', message, 'error');
  }

  showRegularView() {
    this.isInterimView = false;
    this.loadDemandes();
  }
  
  showInterimView() {
    this.isInterimView = true;
    this.loadDemandesForIn();
  }  

  onPageChange(event: any): void {
    this.page = event.page;
    this.size = event.rows;
    this.loadDemandes();
    this.loadDemandesForIn();
  }
  
  onFormSubmitForDem(event: Event): void {
    event.preventDefault();
    this.loadDemandes();
    this.loadDemandesForIn();
  }
  
  toReload(): void {
    this.critere = '';
    this.loadDemandes();
    this.loadDemandesForIn();
  }

  redirectDetails(uid: string): void {
    this.router.navigate(['/details-da', uid]);
  }

  ouvrirLien(lien : string): void {
    if(lien){
      window.open(lien , '_blank', 'noopener,noreferrer');
    }
  }

}
