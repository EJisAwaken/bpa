import { Component } from '@angular/core';
import { UserRequest } from 'src/app/object/userRequest';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { DureeService } from 'src/app/services/duree.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent {
  users: UserRequest[] = [];
  critere = '';
  totalElements = 0;
  page = 0;
  size = 5;
  isLoading = false; 

  constructor(private userService: UserService, private router: Router, private dureeService: DureeService) {}

  ngOnInit() {
    this.getUsersActive();
  }

  getUsersActive(): void {
    this.isLoading = true;

    this.userService.listUserActive(this.page, this.size, this.critere).subscribe({
      next: (response) => {
        this.users = response.content;
        this.totalElements = response.totalElements;
      },
      error: (error) => {
        this.handleError('Votre session est expirée ou une erreur est survenue lors du chargement des données', error);
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  deleteUtilisateur(id: number): void {
    if (!id) {
      Swal.fire('Erreur', 'ID utilisateur invalide.', 'error');
      return;
    }
  
    Swal.fire({
      title: 'Confirmer la suppression',
      text: "Êtes-vous sûr de vouloir supprimer cet utilisateur ?",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Annuler',
    }).then((result) => {
      if (result.isConfirmed) {
        this.isLoading = true;
        this.userService.deleteUser(id).subscribe({
          next: () => {
            this.getUsersActive();
            Swal.fire('Supprimé !', 'Utilisateur supprimé avec succès.', 'success');
          },
          error: (error) => {
            this.isLoading = false;
            this.handleError("Erreur lors de la suppression de l'utilisateur", error);
          },
          complete: () => {
            this.isLoading = false;
          }
        });
      }
    });
  }

  changePassword(id_x3: string): void {
    Swal.fire({
      title: 'Reinitialiser le mot de passe',
      input: 'password',
      inputLabel: 'Nouveau mot de passe',
      inputPlaceholder: 'Entrez le nouveau mot de passe',
      showCancelButton: true,
      confirmButtonText: 'Valider',
      cancelButtonText: 'Annuler',
      inputValidator: (value) => {
        if (!value) {
          return 'Le mot de passe ne peut pas être vide';
        }
        return null;
      }
    }).then((result) => {     
      if (result.isConfirmed && result.value) {
        const newPwd = result.value;
        this.isLoading = true ;
        this.userService.refreshPwd(id_x3, newPwd).subscribe({
          next: () => {
            Swal.fire('Succès', 'Mot de passe mis à jour', 'success');
            this.isLoading = false ;
          },
          error: () => {
            Swal.fire('Erreur', 'Impossible de mettre à jour le mot de passe', 'error');
            this.isLoading = false ;
          }
        });
      }
    });
  }

  aJours():void{
    this.dureeService.upgradeDuree().subscribe({
      next: () => {
        this.getUsersActive();
        Swal.fire('Succès !', 'Donnée à jours', 'success');
      },
      error: (error) => {
        this.isLoading = false;
        this.handleError("Erreur lors de la mis à jours", error);
      },
    });
  }
  
  onPageChange(event: any): void {
    this.page = event.page;
    this.size = event.rows;
    this.getUsersActive();
  }

  onFormSubmit(event: Event): void {
    event.preventDefault();
    this.getUsersActive();
  }

  toReload(): void {
    this.critere = '';
    this.getUsersActive();
  }

  private handleError(message: string, error: any): void {
    console.error(message, error);
    Swal.fire('Erreur', message, 'error');
  }

  redirectAdd(): void {
    this.router.navigate(['/adduser']);
  }
  
  redirectUpdate(id: number): void {
    this.router.navigate(['/updateuser', id]);
  }
  
  redirectLock(id: number): void {
    this.router.navigate(['/access', id]);
  }
}
