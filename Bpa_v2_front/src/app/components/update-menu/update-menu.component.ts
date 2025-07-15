import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Menu } from 'src/app/models/menu';
import { MenuService } from 'src/app/services/menu.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-update-menu',
  templateUrl: './update-menu.component.html',
  styleUrls: ['./update-menu.component.css']
})
export class UpdateMenuComponent {
    id =0;
    menu: Menu={
      idMenu:0,
      role:"",
      label:"",
      icone:"",
      route:""
    }
    rl: Menu | null=null;
    isLoading =false;
  
    constructor(private menuService: MenuService,private route: ActivatedRoute,private router: Router){}
  
    ngOnInit() {
      this.route.params.subscribe(params => {
        this.id = +params['id'];
        if (this.id) {
          this.loadMenuSelected();
        }
      });
    }
  
    loadMenuSelected():void{
      this.menuService.getById(this.id).subscribe(
        (response) =>{
          this.rl=response;
          this.menu ={
            idMenu:response.idMenu,
            role:response.role,
            label:response.label,
            icone:response.icone,
            route:response.route
          }
          this.isLoading=false;
        }
      )
    }
  
    updateRole():void{
      if (!this.menu.idMenu || this.menu.idMenu === 0) {
        Swal.fire('Erreur', 'Utilisateur invalide', 'error');
        return;
      }
    
      this.menuService.updateMenu(this.menu).subscribe({
        next: () => {
          Swal.fire('Succès', 'Menu modifiée avec succès', 'success');
          this.redirectBack();
        },
        error: (error) => {
          this.handleError('Erreur lors de la modification du role', error);
        }
      });
    }
  
    private handleError(message: string, error: any): void {
      console.error(message, error);
      Swal.fire('Erreur', message, 'error');
    }
  
    redirectBack(): void{
      this.router.navigate(['/menu',this.menu.role]);
    }
}
