import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { combineLatest,forkJoin } from 'rxjs';
import { Menu } from 'src/app/models/menu';
import { UserRequest } from 'src/app/object/userRequest';
import { AuthService } from 'src/app/services/auth.service';
import { CommandeService } from 'src/app/services/commande.service';
import { DemandeService } from 'src/app/services/demande.service';
import { DetailService } from 'src/app/services/detail.service';
import { MenuService } from 'src/app/services/menu.service';
import { UserService } from 'src/app/services/user.service';
import { WebSocketService } from 'src/app/services/websocket.service';
import { DetailbcService } from 'src/app/services/detailbc.service';
import { DetailmereService } from 'src/app/services/detailmere.service';
import { FactureService } from 'src/app/services/facture.service';
import { FacturecompService } from 'src/app/services/facturecomp.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.css']
})
export class TopbarComponent {
  role = 0;
  count :number = 0;
  countInterim:number = 0;
  nonLusCount : number = 0;
  utilisateur : UserRequest | null = null;
  dropdownItems : Menu[]=[];
  isLoadingbtn: boolean=false;

  constructor(
    private userService:UserService,
    private menuService: MenuService,
    private webSocketService: WebSocketService,
    private authService: AuthService, 
    private router: Router,
    private demandeService:DemandeService,
    private commandeService:CommandeService,
    private detailService:DetailService,
    private detailbcService : DetailbcService,
    private factureService: FactureService,
    private facturecompService: FacturecompService,
    private detailmereService: DetailmereService
  ){}

  ngOnInit(): void {
    combineLatest([
      this.webSocketService.getCountNotif(),
      this.webSocketService.getCountInterimNotif()
    ]).subscribe(([notifCount, interimCount]) => {
      this.count = notifCount;
      this.countInterim = interimCount;
      this.nonLusCount = notifCount + interimCount;
      console.log("le nombres de notifs interim:",this.countInterim)
      console.log("le nombres de notifs:",this.count)
    });
  
    this.CheckRole();
  }  
  
  disconnect(): void {
    this.webSocketService.disconnect();
  }

  update(): void {
    this.isLoadingbtn = true;
    
    forkJoin([
      this.demandeService.update(),
      this.detailService.update(),
      this.commandeService.update(),
      this.detailbcService.update(),
      this.detailmereService.update(),
      this.factureService.update(),
      this.facturecompService.update()
    ]).subscribe({
      next: (responses) => {
        this.isLoadingbtn = false;
        console.log("Réponses reçues :", responses); // Debugging
        
        if (responses.every(response => typeof response === 'string')) {
          this.showSuccessPopup();
        } else {
          this.showErrorPopup();
        }
      },
      error: (err) => {
        this.isLoadingbtn = false;
        this.showErrorPopup();
        console.error("Erreur lors de la mise à jour :", err);
      }
    });
  }

  showSuccessPopup() {
    Swal.fire({
      icon: 'success',
      title: 'Succès',
      text: 'Les deux mises à jour ont été effectuées avec succès.',
      showConfirmButton: true,
      confirmButtonText: 'OK'
    });
  }
  
  showErrorPopup() {
    Swal.fire({
      icon: 'error',
      title: 'Erreur',
      text: 'Une erreur est survenue lors de la mise à jour. Veuillez réessayer.',
      showConfirmButton: true,
      confirmButtonText: 'OK'
    });
  }  
  
  CheckRole(): void {
    this.userService.getUserConnected().subscribe(
      (response: UserRequest) => {
        this.utilisateur = response;   
        this.getMenu();     
      },
      (error) => {
        console.error('Erreur lors de la récupération de l\'utilisateur connecté', error);
      }
    );
  }

  getMenu() : void {
    if (!this.utilisateur) return;

    this.menuService.getByIdRole(this.utilisateur.role.unique_role).subscribe(
      (data : Menu[]) =>{
        this.dropdownItems = data;
        console.log(this.utilisateur?.role.unique_role);
      }
    );
  }
  
  logout() {
    this.authService.logout(); 
    this.disconnect();
    this.router.navigate(['/login']);
  }
  
  setting() {
    this.router.navigate(['/changePwd']);
  }
  
  notifs() {
    this.router.navigate(['/notif']);
  }
  

  onLogout(): void {
    Swal.fire({
      title: 'Confirmer la déconnexion',
      text: "Êtes-vous sûr de vouloir vous déconnecter ?",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6', 
      cancelButtonColor: '#d33',       
      confirmButtonText: 'Confirmer',
      cancelButtonText: 'Annuler',
      background: '#fff',              
      backdrop: `
        rgba(0,0,0,0.4)
      `,
      customClass: {
        popup: 'custom-swal-popup',
        title: 'custom-swal-title',
        confirmButton: 'custom-swal-confirm-button',
        cancelButton: 'custom-swal-cancel-button',
        icon: 'custom-swal-icon',
      },
      buttonsStyling: true,  
    }).then((result) => {
      if (result.isConfirmed) {
        this.logout();
      }
    });
  }  
}
