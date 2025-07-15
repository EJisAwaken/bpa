import { Component } from '@angular/core';
import { Role } from 'src/app/models/role';
import {RoleService } from 'src/app/services/role.service';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';
import { ActivatedRoute, Router } from '@angular/router';
import { UserRequest } from 'src/app/object/userRequest';
import { User } from 'src/app/models/user';

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css']
})
export class UpdateUserComponent {
  roles : Role[]=[];
  id: number = 0;
  user : User ={
    idUser :0,
    nom:"",
    prenom:"",
    email:"",
    idRole:0,
    mot_de_passe:"",
    id_x3:"",
    interim:""
  }
  userRequest: UserRequest | null = null;
  isLoading = false; 

  constructor(private roleService: RoleService,private userService: UserService,private route: ActivatedRoute,private router: Router){}

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = +params['id'];
      if (this.id) {
        this.loadUserSelected();
      }
    });
    this.loadRoles();
  }

  loadRoles(): void{
    this.isLoading = true; 
    this.roleService.listRoles().subscribe(
      (data : Role[]) =>{
        this.roles=data;
        this.isLoading = false; 
      }
    )
  }

  loadUserSelected(): void {
    this.isLoading = true; 
    this.userService.getUserSelected(this.id).subscribe(
      (response) => {
        if (response) {
          this.userRequest = response;
          this.user = {
            idUser: response.idUser,
            nom: response.nom,
            prenom: response.prenom,
            email: response.email,
            idRole: response.role.idRole,
            id_x3: response.id_x3,  
            interim: response.interim,  
            mot_de_passe: "" 
          };
          this.isLoading = false; 
        }
      }
    );
  }
  

  updateUser(): void {
    if (!this.user.idUser || this.user.idUser === 0) {
      Swal.fire('Erreur', 'Utilisateur invalide', 'error');
      return;
    }
  
    this.userService.updateUser(this.user).subscribe({
      next: () => {
        Swal.fire('Succès', 'Utilisateur modifié avec succès', 'success');
        this.router.navigate(['/users']);
      },
      error: (error) => {
        this.handleError('Erreur lors de la modification de l\'utilisateur', error);
      }
    });
  }
  
  
  private handleError(message: string, error: any): void {
        console.error(message, error);
        Swal.fire('Erreur', message, 'error');
  }

  redirectBack(): void{
    this.router.navigate(['/users']);
  }
}
