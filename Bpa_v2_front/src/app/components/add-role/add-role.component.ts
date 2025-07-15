import { Component } from '@angular/core';
import { Role } from 'src/app/models/role';
import { RoleService } from 'src/app/services/role.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-role',
  templateUrl: './add-role.component.html',
  styleUrls: ['./add-role.component.css']
})
export class AddRoleComponent {
  role : Role={
    idRole : 0,
    unique_role:"",
    role:""
  }
  isLoading = false;

  constructor(private roleService: RoleService,private router: Router){}

  insertRole():void{
    this.isLoading = true;
    this.roleService.saveRole(this.role).subscribe({
          next: () => {
            Swal.fire('Succès', 'Role ajouté avec succès', 'success');
            this.router.navigate(['/roles']);
            this.isLoading = false;
          },
          error: (error) => {
            this.handleError('Erreur lors de l\'ajout du role', error);
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
}
