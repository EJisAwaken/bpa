import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-default',
  templateUrl: './default.component.html',
  styleUrls: ['./default.component.css']
})
export class DefaultComponent {
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
  
  
    constructor(private userService: UserService,private router: Router){}
  
    insertUser(): void{
      this.userService.saveDefaultUser(this.user).subscribe({
        next: () => {
          Swal.fire('Succès', 'Utilisateur ajouté avec succès', 'success');
          this.router.navigate(['/login']);
        },
        error: (error) => {
          this.handleError('Erreur lors de l\'ajout de l\'utilisateur', error);
        }
      });
    }

    private handleError(message: string, error: any): void {
      console.error(message, error);
      Swal.fire('Erreur', message, 'error');
    }
}
