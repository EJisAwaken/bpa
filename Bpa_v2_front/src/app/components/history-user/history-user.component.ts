import { Component, HostListener } from '@angular/core';
import { UserRequest } from 'src/app/object/userRequest';
import { UserService } from 'src/app/services/user.service';
import { ValidationService } from 'src/app/services/validation.service';

@Component({
  selector: 'app-history-user',
  templateUrl: './history-user.component.html',
  styleUrls: ['./history-user.component.css']
})
export class HistoryUserComponent {
  validations: any[]=[];
    page: number = 0;
    isLoading: boolean = false; 
    date1: string = "";
    date2: string = "";

      user: UserRequest | null = null;
  
    constructor(private validationService: ValidationService, private userService: UserService){}
    
    ngOnInit() {
      this.getUserConnected();
    }

    getUserConnected(): void {
      this.isLoading = true;
      this.userService.getUserConnected().subscribe({
        next: (resultat: UserRequest) => {
          this.user = resultat;
          this.getAll();
          this.isLoading = false;
        },
        error: (error) => {
          console.error("Erreur lors de la récupération de l'utilisateur connecté :", error);
        }
      });
    }
  
    getAll(reset: boolean = false): void {
      if (reset) {
        this.page = 0;
        this.validations = []; // Vider les anciennes données pour appliquer le filtre
      }
    
      this.isLoading = true;
      if (!this.user) return;
      this.validationService.getByUser(this.page,this.user.idUser,this.date1, this.date2).subscribe(
        (response: any) => {
          this.validations = [...this.validations, ...response.content];
          this.page++;
          this.isLoading = false;
        },
        (error: any) => {
          console.error('Erreur lors de la récupération des demandes:', error);
          this.isLoading = false;
        }
      );
    }
    
  
    @HostListener('window:scroll', ['$event'])
    onScroll(): void {
      const scrolledHeight = window.innerHeight + window.scrollY;
      const totalHeight = document.documentElement.offsetHeight; 
  
      if (scrolledHeight >= totalHeight) {
        this.getAll();
        this.isLoading = false;
        // alert("teste scroll");
      }
    }
  
    onFormSubmit(event: Event): void {
      event.preventDefault();
      this.getAll(true); // Réinitialise les données avant d'appliquer le filtre
    }  
  
    toReload(): void {
      this.date1 = "";
      this.date2 = "";
      this.getAll(true); // Recharge les données sans filtre
    }
}
