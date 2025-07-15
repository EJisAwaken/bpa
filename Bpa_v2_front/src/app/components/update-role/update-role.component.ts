import { Component } from '@angular/core';
import { Role } from 'src/app/models/role';
import { RoleService } from 'src/app/services/role.service';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-update-role',
  templateUrl: './update-role.component.html',
  styleUrls: ['./update-role.component.css']
})
export class UpdateRoleComponent {
  id =0;
  role: Role={
    idRole:0,
    unique_role:"",
    role:""
  }
  rl: Role | null=null;
  isLoading =false;

  constructor(private roleService: RoleService,private route: ActivatedRoute,private router: Router){}

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = +params['id'];
      if (this.id) {
        this.loadRoleSelected();
      }
    });
  }

  loadRoleSelected():void{
    this.isLoading=true;
    this.roleService.findById(this.id).subscribe(
      (response) =>{
        this.rl=response;
        this.role ={
          idRole:response.idRole,
          unique_role:response.unique_role,
          role:response.role
        }
        this.isLoading=false;
      }
    )
  }

  updateRole():void{
    if (!this.role.idRole || this.role.idRole === 0) {
      Swal.fire('Erreur', 'Utilisateur invalide', 'error');
      return;
    }
  
    this.roleService.updateRole(this.role).subscribe({
      next: () => {
        Swal.fire('Succès', 'Role modifié avec succès', 'success');
        this.router.navigate(['/roles']);
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
    this.router.navigate(['/roles']);
  }
}
