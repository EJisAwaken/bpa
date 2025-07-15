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
  selector: 'app-detail-da-lecteur',
  templateUrl: './detail-da-lecteur.component.html',
  styleUrls: ['./detail-da-lecteur.component.css']
})
export class DetailDaLecteurComponent {
    uid: string = "";
    id_x3: string = "";
    motif: string = "";
    details: Detail[]=[];
    demande: Demande | null =null;
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
        this.id_x3 = params['id_x3'];
        if (this.uid && this.id_x3) {
          this.getDemandeSelected();
          this.getDetails();
          this.isLoading = false;
        }
      });
    }
  
  
    getDemandeSelected():void{
      this.demandeService.getByUidX3(this.uid,this.id_x3).subscribe(
        (response) =>{
            this.demande = response;
            this.isLoading=false;
        }
      )
    }
  
    getDetails(): void{
      this.detailService.getByUid(this.uid,this.id_x3).subscribe(
        (data : Detail[]) => {
          this.details = data;
          this.isLoading=false;
        }
      );    
    }  
    
    private handleError(message: string, error: any): void {
      console.error(message, error);
      Swal.fire('Erreur', message, 'error');
    }
  
    redirectBack(): void{
      this.router.navigate(['/lec-da']);
    }

    ouvrirLien(lien : string): void {
      if(lien){
        window.open(lien , '_blank', 'noopener,noreferrer');
      }
    }
}
