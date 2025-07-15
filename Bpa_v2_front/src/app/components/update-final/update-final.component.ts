import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Finaldestination } from 'src/app/models/finaldestination';
import { FinaldestinationService } from 'src/app/services/finaldestination.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-update-final',
  templateUrl: './update-final.component.html',
  styleUrls: ['./update-final.component.css']
})
export class UpdateFinalComponent {
    id =0;
    finaldestination : Finaldestination = {
      idFinal : 0,
      uid :"",
      designation : ""
    }
    rl: Finaldestination | null=null;
    isLoading =false;
  
    constructor(private finaldestinationservice: FinaldestinationService,private route: ActivatedRoute,private router: Router){}
  
    ngOnInit() {
      this.route.params.subscribe(params => {
        this.id = +params['id'];
        if (this.id) {
          this.loadFinalSelected();
        }
      });
    }
  
    loadFinalSelected():void{
      this.finaldestinationservice.getById(this.id).subscribe(
        (response) =>{
          this.rl=response;
          this.finaldestination ={
            idFinal:response.idFinal,
            uid:response.uid,
            designation:response.designation
          }
          this.isLoading=false;
        }
      )
    }
  
    updateRole():void{
      if (!this.finaldestination.idFinal || this.finaldestination.idFinal === 0) {
        Swal.fire('Erreur', 'Destination invalide', 'error');
        return;
      }
    
      this.finaldestinationservice.update(this.finaldestination).subscribe({
        next: () => {
          Swal.fire('Succès', 'Destination modifiée avec succès', 'success');
          this.redirectBack();
        },
        error: (error) => {
          this.handleError('Erreur lors de la modification de la destination', error);
        }
      });
    }
  
    private handleError(message: string, error: any): void {
      console.error(message, error);
      Swal.fire('Erreur', message, 'error');
    }
  
    redirectBack(): void{
      this.router.navigate(['/final']);
    }
}
