import { Component } from '@angular/core';
import { UserRequest } from 'src/app/object/userRequest';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2/dist/sweetalert2.js';

@Component({
  selector: 'app-change-pwd',
  templateUrl: './change-pwd.component.html',
  styleUrls: ['./change-pwd.component.css']
})
export class ChangePwdComponent {
  utilisateur: UserRequest | null = null;
  id =0;
  ancien='';
  nouvel='';
  confirme='';
  isLoading = false;

  constructor(private userService: UserService){}

  ngOnInit(): void {
    this.CheckRole();
  }

  CheckRole(): void {
    this.isLoading = true;
    this.userService.getUserConnected().subscribe(
      (response: UserRequest) => {
        this.id = response.idUser; 
        this.isLoading = false;         
      },
      (error) => {
        console.error('Erreur lors de la récupération de l\'utilisateur connecté', error);
      }
    );
  }

  onChangePwd(): void {
      if (!this.ancien || !this.nouvel || !this.confirme) {
        Swal.fire({
          icon: 'warning',
          title: 'Champs requis',
          text: 'Veuillez remplir tous les champs.'
        });
        return;
      }

      if (this.nouvel !== this.confirme) {
        Swal.fire({
          icon: 'warning',
          title: 'Mots de passe non identiques',
          text: 'Les mots de passe saisis ne correspondent pas. Veuillez réessayer.'
        });
        return;
      }

      this.userService.changePwd(this.id, this.ancien, this.nouvel).subscribe({
        next: (response: boolean) => { 
          console.log('Réponse du backend:', response);
          
          if (response) {
            Swal.fire({
              icon: 'success',
              title: 'Succès',
              text: 'Le mot de passe a été modifié avec succès.'
            });
            this.isLoading = false;
          } else {
            Swal.fire({
              icon: 'error',
              title: 'Erreur',
              text: 'Échec de la modification du mot de passe.'
            });
          }
        },
        error: (err) => {
          console.log('Erreur capturée:', err);
          
          Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: "Une erreur est survenue lors du changement de mot de passe."
          });
        }
      });    
  }   
}
