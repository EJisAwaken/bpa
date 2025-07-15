import { Component,HostListener } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Menu } from 'src/app/models/menu';
import { UserRequest } from 'src/app/object/userRequest';
import { CommandeService } from 'src/app/services/commande.service';
import { DemandeService } from 'src/app/services/demande.service';
import { DetailService } from 'src/app/services/detail.service';
import { DetailbcService } from 'src/app/services/detailbc.service';
import { DetailmereService } from 'src/app/services/detailmere.service';
import { FactureService } from 'src/app/services/facture.service';
import { FacturecompService } from 'src/app/services/facturecomp.service';
import { MenuService } from 'src/app/services/menu.service';
import { UserService } from 'src/app/services/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  role = "";
  menuItems : Menu[] =[] ;
  utilisateur : UserRequest | null = null;
  isLoading: boolean=false;
  isLoadingbtn: boolean=false;
  isMobile: boolean = false;

  constructor(
    private userService:UserService,
    private demandeService:DemandeService,
    private detailService:DetailService,
    private commandeService:CommandeService,
    private menuService:MenuService,
    private detailbcService : DetailbcService,
    private factureService: FactureService,
    private facturecompService: FacturecompService,
    private detailmereService: DetailmereService
  ){}

  ngOnInit(): void {
    this.CheckRole();
    this.checkScreenSize();
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
  
  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.checkScreenSize();
  }

  checkScreenSize() {
    this.isMobile = window.innerWidth < 768; // Seuil pour mobile
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
    this.isLoading = true;
    this.userService.getUserConnected().subscribe(
      (response: UserRequest) => {
        this.utilisateur = response;
        this.getMenu();
        console.log('is connected with role =',this.role);
        this.isLoading = false;
      },
      (error) => {
        console.error('Erreur lors de la récupération de l\'utilisateur connecté', error);
      }
    );
  }

  getMenu() : void {
    this.isLoading = true ;
    if (!this.utilisateur) return;

    this.menuService.getByIdRole(this.utilisateur.role.unique_role).subscribe(
      (data : Menu[]) =>{
        this.menuItems = data;
      }
    );
  }
}
