import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Menu } from 'src/app/models/menu';
import { MenuService } from 'src/app/services/menu.service';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-add-menu',
  templateUrl: './add-menu.component.html',
  styleUrls: ['./add-menu.component.css']
})
export class AddMenuComponent {
  id : string = "";
  isLoading = false;
  menu : Menu = {
    idMenu : 0,
    role :"",
    label : "",
    icone : "",
    route : ""
  }

  constructor(
    private menuService : MenuService,
    private route: ActivatedRoute,
    private router: Router
  ){}

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = params['id'];
    });
  }

  insertMenu(): void {
    this.isLoading = true;
  
    const menuc = {
      ...this.menu,
      role: this.id
    };
  
    this.menuService.saveMenu(menuc).subscribe({
      next: () => {
        Swal.fire('Succès', 'Menu ajouté avec succès', 'success');
        this.redirectBack();
        this.isLoading = false;
      },
      error: (error) => {
        this.handleError('Erreur lors de l\'ajout de la menu', error);
        this.isLoading = false;
      }
    });
  }
  

  private handleError(message: string, error: any): void {
      console.error(message, error);
      Swal.fire('Erreur', message, 'error');
  }

  redirectBack(): void{
    this.router.navigate(['/menu',this.id]);
  }
}
