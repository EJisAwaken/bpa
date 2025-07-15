import { Component } from '@angular/core';
import { Role } from 'src/app/models/role';
import { User } from 'src/app/models/user';
import {RoleService } from 'src/app/services/role.service';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent {
  roles : Role[]=[];
  user : User ={
    idUser:0,
    nom:"",
    prenom:"",
    email:"",
    idRole:0,
    mot_de_passe:"",
    id_x3:"",
    interim:""
  }
  isLoading = false;

  ngOnInit() {
    this.loadRoles();
  }

  constructor(private roleService: RoleService,private userService: UserService,private router: Router){}

  insertUser(): void{
    this.isLoading = true;
    this.userService.saveNewUser(this.user).subscribe({
      next: () => {
        Swal.fire('Succès', 'Utilisateur ajouté avec succès', 'success');
        this.router.navigate(['/users']);
        this.isLoading = false;
      },
      error: (error) => {
        this.handleError('Erreur lors de l\'ajout de l\'utilisateur', error);
      }
    });
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

  private handleError(message: string, error: any): void {
    console.error(message, error);
    Swal.fire('Erreur', message, 'error');
  }

  redirectBack(): void{
    this.router.navigate(['/users']);
  }
}
