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
  selector: 'app-detail-fc-lecteur',
  templateUrl: './detail-fc-lecteur.component.html',
  styleUrls: ['./detail-fc-lecteur.component.css']
})
export class DetailFcLecteurComponent {
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
  
      private handleError(message: string, error: any): void {
        console.error(message, error);
        Swal.fire('Erreur', message, 'error');
      }
    
      redirectBack(): void{
        this.router.navigate(['/lec-fc']);
      }
}
