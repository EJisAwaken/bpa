
import { Injectable } from '@angular/core';
import { Client, Message } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { Subject } from 'rxjs';
import { UserService } from './user.service';
import { UserRequest } from '../object/userRequest';
import { Notification } from '../models/notification';
import { environment } from './confURL/environment';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient!: Client;
  private notificationSubject = new Subject<Notification>();
  private countSubject = new Subject<number>();
  private countInterimSubject = new Subject<number>();

  constructor(private userService: UserService) {
    this.userService.getUserConnected().subscribe(
      (response: UserRequest) => {
        this.stompClient = new Client({
          brokerURL: environment.socketUrl,
          connectHeaders: {},
          debug: (str) => console.log(str),
          reconnectDelay: 5000,
          heartbeatIncoming: 4000,
          heartbeatOutgoing: 4000
        });

        this.stompClient.onConnect = (frame) => {
          console.log('WebSocket connecté', frame);

          if (response && response.id_x3) {
            this.stompClient.subscribe(`/topic/notifications/${response.id_x3}`, (message: Message) => {
              const notif: Notification = JSON.parse(message.body);
              this.notificationSubject.next(notif);
            });
            this.stompClient.subscribe(`/topic/count/${response.id_x3}`,(message:Message)=>{
              const count: number = parseInt(message.body, 10);
              this.countSubject.next(count);
              console.log(response.email);
            });
            this.stompClient.subscribe(`/topic/countInterim/${response.interim}`,(message:Message)=>{
              const count: number = parseInt(message.body, 10);
              this.countInterimSubject.next(count);
              console.log(response.email);
            });
            
          }
        };

        this.stompClient.onWebSocketError = (error) => {
          console.error('Erreur WebSocket', error);
        };

        this.stompClient.activate();
      }
    );
  }

  getNotifications() {
    return this.notificationSubject.asObservable();
  }

  getCountNotif(){
    return this.countSubject.asObservable();
  }
  
  getCountInterimNotif(){
    return this.countInterimSubject.asObservable();
  }

  disconnect() {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.deactivate();
    }
  }
}