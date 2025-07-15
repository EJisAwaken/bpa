import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Role } from 'src/app/models/role';
import { RoleService } from 'src/app/services/role.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-roles',
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.css']
})
export class RolesComponent {
  roles: Role[] =[];
  isLoading = false;

  constructor(private roleService: RoleService,private router: Router){}

  ngOnInit() {
    this.loadRoles();
  }

  loadRoles():void{
    this.isLoading= true;
    this.roleService.listRoles().subscribe(
      (data : Role[]) =>{
        this.roles=data;
        this.isLoading = false; 
      }
    );
  }

  delete(id: number):void{
    if (!id) {
      Swal.fire('Erreur', 'ID role invalide.', 'error');
      return;
    }
  
    Swal.fire({
      title: 'Confirmer la suppression',
      text: "Êtes-vous sûr de vouloir supprimer cet role ?",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Annuler',
    }).then((result) => {
      if (result.isConfirmed) {
        this.isLoading = true;
        this.roleService.deleteRole(id).subscribe({
          next: () => {
            this.loadRoles();
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

  redirectAdd(): void{
    this.router.navigate(['/addrole']);
  }

  redirectUpdate(id: number): void {
    this.router.navigate(['/updaterole', id]);
  }
  
  redirectMenu(id: string): void {
    this.router.navigate(['/menu', id]);
  }
}
