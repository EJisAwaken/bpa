import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Menu } from 'src/app/models/menu';
import { MenuService } from 'src/app/services/menu.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent {
  menus : Menu[] = [];
  id : string = '';
  isLoading = false;

  constructor(
    private menuService : MenuService,
    private route: ActivatedRoute,
    private router: Router
  ){}

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = params['id'];
      if (this.id) {
        this.getMenu();
        this.isLoading = false;
      }
    });
  }

  getMenu():void {
    if (!this.id) return;

    this.menuService.getByIdRole(this.id).subscribe(
      (data : Menu[]) => {
        this.menus = data ;
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
      text: "Êtes-vous sûr de vouloir supprimer cette menu ?",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Annuler',
    }).then((result) => {
      if (result.isConfirmed) {
        this.isLoading = true;
        this.menuService.deleteMenu(id).subscribe({
          next: () => {
            this.getMenu();
            Swal.fire('Supprimé !', 'Role supprimé avec succès.', 'success');
          },
          error: (error) => {
            this.isLoading = false;
            this.handleError("Erreur lors de la suppression du role", error);
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

  redirectBack(): void{
    this.router.navigate(['/roles']);
  }
  
  redirectAdd(): void{
    this.router.navigate(['/add-menu',this.id]);
  }
  
  redirectUpdate(id : number): void{
    this.router.navigate(['/updatemenu',id]);
  }
}
