import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Commande } from 'src/app/models/commande';
import { Detailbc } from 'src/app/models/detailbc';
import { Validation } from 'src/app/models/validation';
import { CommandeService } from 'src/app/services/commande.service';
import { DetailbcService } from 'src/app/services/detailbc.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-detail-bc-lecteur',
  templateUrl: './detail-bc-lecteur.component.html',
  styleUrls: ['./detail-bc-lecteur.component.css']
})
export class DetailBcLecteurComponent {
    uid: string = "";
    id_x3: string = "";
    motif: string = "";
    details: Detailbc[]=[];
    commande: Commande | null = null;
    validation: Validation | null=null;
    isAprouve: boolean = false;
    isLoading: boolean = false;

    constructor(
      private route: ActivatedRoute,
      private commandeService: CommandeService,
      private detailbcService: DetailbcService,
      private router: Router){}

    ngOnInit() {
      this.route.params.subscribe(params => {
        this.uid = params['uid'];
        this.id_x3 = params['id_x3'];
        if (this.uid && this.id_x3) {
          this.getCommandeSelected();
          this.getDetails();
          this.isLoading = false;
        }
      });
    }

    getCommandeSelected():void{
      this.commandeService.getByUidIdx3(this.uid,this.id_x3).subscribe(
        (response) =>{
            this.commande = response;
            this.isLoading=false;
        }
      )
    }
  
    getDetails(): void{
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

    private handleError(message: string, error: any): void {
      console.error(message, error);
      Swal.fire('Erreur', message, 'error');
    }
  
    redirectBack(): void{
      this.router.navigate(['/lec-bc']);
    }

    ouvrirLien(lien : string): void {
      if(lien){
        window.open(lien , '_blank', 'noopener,noreferrer');
      }
    }
}
