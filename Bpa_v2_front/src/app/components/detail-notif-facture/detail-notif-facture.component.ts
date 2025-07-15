import { Component } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { ActivatedRoute,Router } from '@angular/router';
import { Detailmere } from 'src/app/models/detailmere';
import { Facture } from 'src/app/models/facture';
import { Facturecomp } from 'src/app/models/facturecomp';
import { Facturecompett } from 'src/app/models/facturecompett';
import { UserRequest } from 'src/app/object/userRequest';
import { DetailmereService } from 'src/app/services/detailmere.service';
import { FactureService } from 'src/app/services/facture.service';
import { FacturecompService } from 'src/app/services/facturecomp.service';
import { FacturecompettService } from 'src/app/services/facturecompett.service';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-detail-notif-facture',
  templateUrl: './detail-notif-facture.component.html',
  styleUrls: ['./detail-notif-facture.component.css']
})
export class DetailNotifFactureComponent {
    uid: string = "";
    isLoading: boolean = false;
    facture : Facture  | null=null;
    facomps : Facturecomp [] = [];
    facompett : Facturecompett | null=null;
    details : Detailmere [] = [];
    user : UserRequest | null=null;
    motif = "";
    constructor(
      private route: ActivatedRoute,
      private factureService: FactureService,
      private facturecompService: FacturecompService,
      private facturecompettService: FacturecompettService,
      private detailmereService: DetailmereService,
      private userService: UserService,
      private router: Router
    ){}
    
    
    ngOnInit() {
      this.route.params.subscribe(params => {
        this.uid = params['uid'];
        if (this.uid) {
          this.isLoading = false;
          this.getByUid();
          this.getByMere();
          this.getUserConnected();
        }
      });
    }

    getUserConnected(): void {
      this.userService.getUserConnected().subscribe(
        (resultat: UserRequest) => {
          this.user = resultat;
          this.isLoading = false;
        },
        (error) => {
          console.error("Erreur lors de la récupération de l'utilisateur connecté :", error);
        }
      );
    }
  

    getByUid():void{
      if (!this.uid) return;

      this.facturecompettService.getByUid(this.uid).subscribe(
        (response : Facturecompett)=>{
          this.facompett=response;
          this.isLoading = false;
        }
      );
    }
    
    getByMere():void{
      if (!this.uid) return;

      this.facturecompService.getByUid(this.uid).subscribe(
        (data : Facturecomp[])=>{
          this.facomps=data;
          this.isLoading = false;
        }
      );
    }

    getDetailsmereByMere(uid : string):void{
      this.detailmereService.getByMere(uid).subscribe(
        (data : Detailmere[]) => {
          this.details = data;
          this.isLoading = false;
        }
      );
    }
    
    getMere(uid : string):void{
      this.factureService.getByUid(uid).subscribe(
        (data : Facture) => {
          this.facture = data;
          this.isLoading = false;
        }
      );
    }


    async showInfoPopup(uid: string) {
      this.isLoading = true;
    
      try {
        const [details, facture] = await Promise.all([
          firstValueFrom(this.detailmereService.getByMere(uid)),
          firstValueFrom(this.factureService.getByUid(uid))
        ]);
    
        this.details = details || [];      // fallback si undefined
        this.facture = facture || null;    // fallback si undefined
        this.isLoading = false;
    
        const detailsRows = this.details.map(detail => `
          <tr>
            <td>${detail.objet}</td>
            <td>${detail.quantite}</td>
            <td>${detail.pu}</td>
            <td>${detail.montant}</td>
            <td>${detail.devise}</td>
          </tr>
        `).join('');
    
        Swal.fire({
          title: '<span style="font-size: 20px;">Détails de la facture mère</span>',
          html: `
            <div style="text-align: left; font-size: 16px; line-height: 1.6;">
              <p><strong>N° facture :</strong> <span style="color: #0032A0; font-weight: bold;">${this.facture?.uid}</span></p>
              <p><strong>Société :</strong> <span style="color: #0032A0;">${this.facture?.societe}</span></p>
              <p><strong>Fournisseur :</strong> <span style="color: #0032A0;">${this.facture?.fournisseur}</span></p>
              <p><strong>Date :</strong> <span style="color: #0032A0;">${this.facture?.date}</span></p>
            </div>
            <div class="table-responsive" style="font-size: 11px;">
              <table class="table table-bordered" width="100%" cellspacing="0">
                <thead>
                    <tr class="text-center">
                        <th>Article</th>
                        <th>Quantité</th>
                        <th>PU</th>
                        <th>Montant</th>
                        <th>Devise</th>
                    </tr>
                </thead>
                <tbody>
                    ${detailsRows}
                </tbody>
              </table>
            </div>
          `,
          icon: 'info',
          showCloseButton: true,
          focusConfirm: false,
          confirmButtonText: 'Fermer',
          customClass: {
            popup: 'custom-popup-class'
          }
        });
    
      } catch (error) {
        this.isLoading = false;
        this.handleError("Impossible de charger les données de la facture mère.", error);
      }
    }
    
    validation(uid:string):void{
      if (!this.user) return;

      Swal.fire({
            title: 'Confirmer la validation',
            text: "Êtes-vous sûr de valider cette facture complementaire?",
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Valider',
            cancelButtonText: 'Annuler'
          }).then((result) => {
            if (result.isConfirmed && this.user) {
              this.isLoading = true;
        
              const valeur1 = 4;
              const idetat = 2;
        
              this.facturecompettService
                .validation(uid,this.user.id_x3,this.motif,valeur1,idetat)
                .subscribe({
                  next: () => {
                    Swal.fire('Succès !', 'La facture a été validée avec succès.', 'success')
                      .then(() => {
                        this.getByMere();
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

    refus(uid:string,idetat:number,valeur1:number):void {
      if (!this.user) return;

      Swal.fire({
          title: 'Confirmer le refus',
          text: "Êtes-vous sûr de refuser cette facture complementaire ?",
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
          
                this.facturecompettService
                  .validation(uid,this.user.id_x3,this.motif,valeur1,idetat)
                  .subscribe({
                    next: () => {
                      Swal.fire('Succès !', 'La facture a été refusée.', 'success')
                        .then(() => {
                          this.getByMere();
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
      this.router.navigate(['/notif']);
    }
}
