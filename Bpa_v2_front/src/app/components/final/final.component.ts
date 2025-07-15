import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Finaldestination } from 'src/app/models/finaldestination';
import { FinaldestinationService } from 'src/app/services/finaldestination.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-final',
  templateUrl: './final.component.html',
  styleUrls: ['./final.component.css']
})
export class FinalComponent {
  finaldestinations : Finaldestination[] = [];
  id : string = '';
  isLoading = false;

  constructor(
    private finaldestinationService : FinaldestinationService,
    private route: ActivatedRoute,
    private router: Router
  ){}

  ngOnInit() {
    // this.route.params.subscribe(params => {
    //   this.id = params['id'];
    //   if (this.id) {
    //     this.getMenu();
    //     this.isLoading = false;
    //   }
    // });
    this.getFinaldestination();
  }

  getFinaldestination():void {
    this.finaldestinationService.getAll().subscribe(
      (data : Finaldestination[]) => {
        this.finaldestinations = data ;
        this.isLoading = false;
      }
    );
  }

  delete(id: number):void{
    if (!id) {
      Swal.fire('Erreur', 'ID menu invalide.', 'error');
      return;
    }
  
    Swal.fire({
      title: 'Confirmer la suppression',
      text: "Êtes-vous sûr de vouloir supprimer cette destinantion?",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Annuler',
    }).then((result) => {
      if (result.isConfirmed) {
        this.isLoading = true;
        this.finaldestinationService.delete(id).subscribe({
          next: () => {
            this.getFinaldestination();
            Swal.fire('Supprimé !', 'Destination supprimé avec succès.', 'success');
          },
          error: (error) => {
            this.isLoading = false;
            this.handleError("Erreur lors de la suppression du destinantion", error);
          },
          complete: () => {
            this.isLoading = false;
          }
        });
      }
    });
  }

  private handleError(message: string, error: any): void {
    console.error(message, error);
    Swal.fire('Erreur', message, 'error');
  }
  
  redirectAdd(): void{
    this.router.navigate(['/addfinal']);
  }
  
  redirectUpdate(id : number): void{
    this.router.navigate(['/updatefinal',id]);
  }
}
