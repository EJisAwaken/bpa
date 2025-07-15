import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Commande } from 'src/app/models/commande';
import { Demande } from 'src/app/models/demande';
import { Detailbc } from 'src/app/models/detailbc';
import { Validation } from 'src/app/models/validation';
import { UserRequest } from 'src/app/object/userRequest';
import { CommandeService } from 'src/app/services/commande.service';
import { DetailbcService } from 'src/app/services/detailbc.service';
import { UserService } from 'src/app/services/user.service';
import { ValidationService } from 'src/app/services/validation.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-detail-commande',
  templateUrl: './detail-commande.component.html',
  styleUrls: ['./detail-commande.component.css']
})
export class DetailCommandeComponent {
    user: UserRequest | null = null;
    uid: string = "";
    motif: string = "";
    details: Detailbc[]=[];
    commandes: Commande[]=[];
    commande: Commande | null = null;
    validation: Validation | null=null;
    isAprouve: boolean = false;
    isLoading: boolean = false;

    constructor(
      private route: ActivatedRoute,
      private validationService: ValidationService,
      private userService: UserService,
      private commandeService: CommandeService,
      private detailbcService: DetailbcService,
      private router: Router){}

    ngOnInit() {
      this.route.params.subscribe(params => {
        this.uid = params['uid'];
        if (this.uid) {
          this.getCommandeSelected();
          this.checkAprouve();
          this.getDetails();
          this.isLoading = false;
        }
      });
      this.getUserConnected();
    }

    getCommandeSelected():void{
      if(this.user){
        this.commandeService.findByUid(this.uid,this.user.id_x3).subscribe(
          (data :Commande[]) =>{
            if(data){
              this.commande = data[0];
              this.checkAprouve();
              this.isLoading=false;
            }else{
              if(this.user?.interim){
                this.commandeService.findByUid(this.uid,this.user.interim).subscribe(
                  (data :Commande[]) =>{
                    this.commande = data[0];
                    this.checkAprouve();
                    this.isLoading=false;
                  }
                )
              }
            }
          }
        )
      }
    }
  
    getUserConnected(): void {
      this.userService.getUserConnected().subscribe(
        (resultat: UserRequest) => {
          this.user = resultat;
          this.getCommandeSelected();
          this.getDetails();
          this.isLoading = false;
        },
        (error) => {
          console.error("Erreur lors de la récupération de l'utilisateur connecté :", error);
        }
      );
    }

     getDetails(): void{
        if(this.user){
          this.detailbcService.getByUid(this.uid).subscribe(
            (data : Detailbc[]) => {
              this.details = data;
              this.isLoading=false;
            }
          )
          this.detailbcService.getByUid(this.uid).subscribe(
            (data : Detailbc[]) => {
              this.details = data;
              this.isLoading=false;
            }
          )
        }
      }

    checkAprouve():void{
      if(this.commande){
        if(this.commande.etat.idEtat ==2 || this.commande.etat.idEtat ==3){
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
        text: "Êtes-vous sûr de valider cette commande ?",
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
          const valeur1 = 3;
          const idetat = 2;
          const designation = 'VAL';
    
          // Appel direct au service
          this.commandeService
            .validation(uid, this.user.id_x3, designation, valeur1,idetat,this.motif)
            .subscribe({
              next: () => {
                Swal.fire('Succès !', 'La commande a été validée avec succès.', 'success')
                  .then(() => {
                    this.getCommandeSelected();
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
        text: "Êtes-vous sûr de refuser cette commande ?",
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
              const idetat = 3;
              const designation = 'REJ';
        
              this.commandeService
                .validation(uid, this.user.id_x3, designation, valeur1,idetat,this.motif)
                .subscribe({
                  next: () => {
                    Swal.fire('Succès !', 'La commande a été refusée avec succès.', 'success')
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
  
    redirectBack(): void{
      this.router.navigate(['/list-bc']);
    }

    ouvrirLien(lien : string): void {
      if(lien){
        window.open(lien , '_blank', 'noopener,noreferrer');
      }
    }
}
