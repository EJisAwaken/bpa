import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Finaldestination } from 'src/app/models/finaldestination';
import { FinaldestinationService } from 'src/app/services/finaldestination.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-final',
  templateUrl: './add-final.component.html',
  styleUrls: ['./add-final.component.css']
})
export class AddFinalComponent {
  isLoading = false;
  finaldestination : Finaldestination = {
    idFinal : 0,
    uid :"",
    designation : ""
  }

  constructor(
    private finaldestinationService : FinaldestinationService,
    private route: ActivatedRoute,
    private router: Router
  ){}

  ngOnInit() {
    
  }

  insertMenu(): void {
    this.isLoading = true;

  
    this.finaldestinationService.insert(this.finaldestination).subscribe({
      next: () => {
        Swal.fire('Succès', 'Destination ajouté avec succès', 'success');
        this.redirectBack();
        this.isLoading = false;
      },
      error: (error) => {
        this.handleError('Erreur lors de l\'ajout de la destination', error);
        this.isLoading = false;
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
