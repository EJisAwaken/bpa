import { Component, OnInit } from '@angular/core';
import { WebSocketService } from '../../services/websocket.service';
import { NotificationService } from 'src/app/services/notification.service';
import { Notification } from 'src/app/models/notification';
import { UserService } from 'src/app/services/user.service';
import { UserRequest } from 'src/app/object/userRequest';
import { Router } from '@angular/router';
import { Observable,combineLatest } from 'rxjs';

import Swal from 'sweetalert2';
import { LocalizedString } from '@angular/compiler';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit {
  notifs: Notification[] =[];
  notifsInterim: Notification[] =[];
  isBalanceVisible = true;
  user: UserRequest | null = null;
  count :number = 0;
  countInterim:number = 0;
  isInterimView = false;
  tableData = [
    {
      name: "James Butt",
      id: "1000",
      country: "Algeria",
      date: "2015-09-13",
      company: "Benton, John B Jr",
      status: "unqualified",
      activity: "17",
      representative: "Ioni Bowcher",
      balance: "$70,663.00"
    },
    // Ajoute d'autres données ici...
  ];

  constructor(private webSocketService: WebSocketService, private router: Router,private userService: UserService,private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.webSocketService.getNotifications().subscribe(message => {
      this.notifs.push(message);
    });
    combineLatest([
      this.webSocketService.getCountNotif(),
      this.webSocketService.getCountInterimNotif()
    ]).subscribe(([notifCount, interimCount]) => {
      this.count = notifCount;
      this.countInterim = interimCount;
    });
    this.getUserConnected();
  }

  getUserConnected(): void {
    this.userService.getUserConnected().subscribe(
      (resultat: UserRequest) => {
        this.user = resultat;
        this.loadNotification();
        this.loadNotificationForInterim();
      },
      (error) => {
        console.error("Erreur lors de la récupération de l'utilisateur connecté :", error);
      }
    );
  }

  loadNotification(): void {
    if (!this.user) return;

    const direction = this.user.id_x3 || '';

    this.notificationService.getNotification(this.user.id_x3).subscribe(
      (data :Notification[]) =>{
        this.notifs=data;
      }
    )
  }
 
  loadNotificationForInterim(): void {
    if (!this.user) return;

    const direction = this.user.interim || '';

    this.notificationService.getNotification(this.user.interim).subscribe(
      (data :Notification[]) =>{
        this.notifsInterim= data;
      }
    )
  }
  
  ouvrir(uid:string,id_x3: string):void{
    this.notificationService.openNotif(uid,id_x3).subscribe({
      next : ()=>{
        this.redirectDetails(uid,id_x3);
      },
      error: (error: any) => {
        this.handleError('Erreur lors de l\'ouverture', error);
      }
    })
  }

  getTimeAgo(dateString: string): string {
    const now = new Date();
    const notifDate = new Date(dateString);
    const diffMs = now.getTime() - notifDate.getTime();
  
    const seconds = Math.floor(diffMs / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours   = Math.floor(minutes / 60);
    const days    = Math.floor(hours / 24);
  
    if (days > 0) return `il y a ${days} jour${days > 1 ? 's' : ''} `;
    if (hours > 0) return `il y a ${hours} heure${hours > 1 ? 's' : ''} `;
    if (minutes > 0) return `il y a ${minutes} minute${minutes > 1 ? 's' : ''} `;
    return `il y a ${seconds} seconde${seconds > 1 ? 's' : ''}`;
  }
  
  
  private handleError(message: string, error: any): void {
    console.error(message, error);
    Swal.fire('Erreur', message, 'error');
  }

  toggleBalance() {
    this.isBalanceVisible = !this.isBalanceVisible;
  }

  redirectDetails(uid: string,id_x3: string): void {
      if (id_x3 == "admin") {
        this.router.navigate(['/detail_notif_facture', uid]);
      }else{
        this.router.navigate(['/detail_notif', uid]);
      }
      
  }

  showRegularView() {
    this.isInterimView = false;
    this.putCountZero();
  }
  
  showInterimView() {
    this.isInterimView = true;
    this.putCountInterimZero();
  }

  putCountZero(): void {
    if(this.user && this.user.id_x3){
      this.notificationService.putZero(this.user.id_x3).subscribe({
        next: () => {
          this.count = 0; 
        },
        error: (err) => {
          console.error("Erreur lors de la mise à zéro des notifications", err);
        }
      });
    }
    
  }

  putCountInterimZero(): void {
    if(this.user && this.user.interim){
      this.notificationService.putZero(this.user.interim).subscribe({
        next: () => {
          this.count = 0; 
        },
        error: (err) => {
          console.error("Erreur lors de la mise à zéro des notifications", err);
        }
      });
    }
    
  }
}

