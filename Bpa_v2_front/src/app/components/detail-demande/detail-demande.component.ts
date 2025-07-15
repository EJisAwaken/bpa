import { Component } from '@angular/core';
import { ActivatedRoute,Router } from '@angular/router';
import { Demande } from 'src/app/models/demande';
import { Detail } from 'src/app/models/detail';
import { Validation } from 'src/app/models/validation';
import { UserRequest } from 'src/app/object/userRequest';
import { DemandeService } from 'src/app/services/demande.service';
import { DetailService } from 'src/app/services/detail.service';
import { UserService } from 'src/app/services/user.service';
import { ValidationService } from 'src/app/services/validation.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-detail-demande',
  templateUrl: './detail-demande.component.html',
  styleUrls: ['./detail-demande.component.css']
})
export class DetailDemandeComponent {
  user: UserRequest | null = null;
  uid: string = "";
  motif: string = "";
  details: Detail[]=[];
  demande: Demande | null =null;
  validation: Validation | null=null;
  isAprouve: boolean = false;
  isLoading: boolean = false;
  constructor(
    private route: ActivatedRoute,
    private validationService: ValidationService,
    private userService: UserService,
    private demandeService: DemandeService,
    private detailService: DetailService,
    private router: Router){}
  
  
  ngOnInit() {
    this.route.params.subscribe(params => {
      this.uid = params['uid'];
      if (this.uid) {
        this.getDemandeSelected();
        this.checkAprouve();
        this.getDetails();
        this.isLoading = false;
      }
    });
    this.getUserConnected();
  }

  getUserConnected(): void {
    this.userService.getUserConnected().subscribe(
      (resultat: UserRequest) => {
        this.user = resultat;
        this.getDemandeSelected();
        this.getDetails();
        this.isLoading = false;
      },
      (error) => {
        console.error("Erreur lors de la récupération de l'utilisateur connecté :", error);
      }
    );
  }


  getDemandeSelected():void{
    if(this.user){
      this.demandeService.getByUidX3(this.uid,this.user?.id_x3).subscribe(
        (response) =>{
          if(response){
            this.demande = response;
            this.isLoading=false;
            this.checkAprouve();
          }else{
            if(this.user?.interim){
              this.demandeService.getByUidX3(this.uid,this.user.interim).subscribe(
                (response)=>{
                  this.demande = response;
                  this.isLoading=false;
                  this.checkAprouve();
                }
              )
            }
          }
        }
      )
    }
  }

  getDetails(): void{
    if(this.user){
      this.detailService.getByUid(this.uid,this.user?.id_x3).subscribe(
        (data : Detail[]) => {
          this.details = data;
          this.isLoading=false;
        }
      )
      this.detailService.getByUid(this.uid,this.user?.interim).subscribe(
        (data : Detail[]) => {
          this.details = data;
          this.isLoading=false;
        }
      )
    }
  }


  checkAprouve():void{
    if(this.demande){
      if(this.demande.etat.idEtat ==2 || this.demande.etat.idEtat ==3){
        this.isAprouve= true;
        this.isLoading=false;
      }else{
        this.isAprouve = false;
        this.isLoading=false;
      }
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
        const idetat = 2;
        const designation = 'VAL';
        this.demandeService
          .validation(uid, this.user.id_x3, designation, valeur1, valeur2, valeur3,idetat,this.motif)
          .subscribe({
            next: () => {
              Swal.fire('Succès !', 'La demande a été validée avec succès.', 'success')
                .then(() => {
                  this.getDemandeSelected();
                  this.checkAprouve();
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
  toValidArticle(uid: string,ref_dem: string,code_article:string): void {
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
  
        // Valeurs par défaut
        const valeur1 = 2;
        const valeur2 = 3;
        const valeur3 = 5;
        const idetat = 2;
        const designation = 'VAL';
  
        // Appel direct au service
        this.detailService
          .validation(uid,ref_dem,code_article, this.user.id_x3, designation, valeur1, valeur2, valeur3,idetat,this.motif)
          .subscribe({
            next: () => {
              Swal.fire('Succès !', 'La demande a été validée avec succès.', 'success')
                .then(() => {
                  this.getDemandeSelected();
                  this.checkAprouve();
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
            const idetat = 3;
            const designation = 'REJ';
      
            this.demandeService
              .validation(uid, this.user.id_x3, designation, valeur1, valeur2, valeur3,idetat,this.motif)
              .subscribe({
                next: () => {
                  Swal.fire('Succès !', 'La demande a été refusée avec succès.', 'success')
                    .then(() => {
                      this.getDemandeSelected();
                      this.checkAprouve();
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

  toRefuseArticle(uid: string,ref_dem:string,code_article:string): void { 
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
            const valeur1 = 2;
            const valeur2 = 1;
            const valeur3 = 5;
            const idetat = 3;
            const designation = 'REJ';
      
            this.detailService
              .validation(uid,ref_dem,code_article, this.user.id_x3, designation, valeur1, valeur2, valeur3,idetat,this.motif)
              .subscribe({
                next: () => {
                  Swal.fire('Succès !', 'La demande a été refusée avec succès.', 'success')
                    .then(() => {
                      this.getDemandeSelected();
                      this.checkAprouve();
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

  redirectBack(): void{
    this.router.navigate(['/list-da']);
  }

  ouvrirLien(lien : string): void {
    if(lien){
      window.open(lien , '_blank', 'noopener,noreferrer');
    }
  }
}
