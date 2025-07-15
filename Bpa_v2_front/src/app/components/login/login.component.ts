import { Component,HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';
import { IsLogged } from 'src/app/models/isLogged';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  user = {
    email: '',
    mot_de_passe: ''
  };
  isLoading: boolean = false;
  isMobile: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    this.isLoading = true;

    this.authService.login(this.user).subscribe(
      (data: IsLogged) => {
        setTimeout(() => {
          this.router.navigate([data.role === 'admin' ? '/dash-admin' : '/dash-val']);
          this.isLoading = false;
        },
      );
      },
      (error: any) => {
        setTimeout(() => {
          Swal.fire({
            title: 'Erreur de connexion',
            text: 'Email ou mot de passe incorrect. Veuillez réessayer.',
            icon: 'error',
            confirmButtonText: 'OK'
          });
          this.isLoading = false;
        }, 1000);
      }
    );
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.checkScreenSize();
  }

  checkScreenSize() {
    this.isMobile = window.innerWidth < 768; // Seuil pour mobile
  }
}
