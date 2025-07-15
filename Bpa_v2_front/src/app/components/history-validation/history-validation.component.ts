import { Component,HostListener} from '@angular/core';
import { Validation } from 'src/app/models/validation';
import { UserRequest } from 'src/app/object/userRequest';
import { UserService } from 'src/app/services/user.service';
import { ValidationService } from 'src/app/services/validation.service';

@Component({
  selector: 'app-history-validation',
  templateUrl: './history-validation.component.html',
  styleUrls: ['./history-validation.component.css']
})
export class HistoryValidationComponent {
  validations: any[]=[];
  users : UserRequest [] =[];
  idUser : number = 0;
  page: number = 0;
  isLoading: boolean = false; 
  date1: string = "";
  date2: string = "";

  constructor(private validationService: ValidationService,private userService: UserService){}
  
  ngOnInit() {
    this.getAll();
    this.getAllUsers();
  }

  getAll(reset: boolean = false): void {
    if (reset) {
      this.page = 0;
      this.validations = []; // Vider les anciennes données pour appliquer le filtre
    }
  
    this.isLoading = true;
    this.validationService.getAll(this.page, this.date1, this.date2,this.idUser).subscribe(
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

  getAllUsers(){
    this.userService.listAllUsers().subscribe(
      (data : UserRequest[])=>{
        this.users = data;
        this.isLoading =false;
      }
    )
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
    this.getAll(true); 
  }  

  toReload(): void {
    this.date1 = '';
    this.date2 = '';
    this.idUser = 0;
    this.getAll(true); 
  }
  
}
