import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent} from './components/login/login.component';
import { DetailDemandeComponent } from './components/detail-demande/detail-demande.component';
import { UsersComponent } from './components/users/users.component';
import { AddUserComponent } from './components/add-user/add-user.component';
import { UpdateUserComponent } from './components/update-user/update-user.component';
import { ChangePwdComponent } from './components/change-pwd/change-pwd.component';
import { MissingPwdComponent } from './components/missing-pwd/missing-pwd.component';
import { DefaultComponent } from './components/default/default.component';
import { HistoryValidationComponent } from './components/history-validation/history-validation.component';
import { HistoryUserComponent } from './components/history-user/history-user.component';
import { NotificationComponent } from './components/notification/notification.component';
import { ListDemandeComponent } from './components/list-demande/list-demande.component';
import { ListCommandeComponent } from './components/list-commande/list-commande.component';
import { DetailNotifComponent } from './components/detail-notif/detail-notif.component';
import { DashboardAdminComponent } from './components/dashboard-admin/dashboard-admin.component';
import { DashboardValidateurComponent } from './components/dashboard-validateur/dashboard-validateur.component';
import { DetailCommandeComponent } from './components/detail-commande/detail-commande.component';
import { RolesComponent } from './components/roles/roles.component';
import { AddRoleComponent } from './components/add-role/add-role.component';
import { UpdateRoleComponent } from './components/update-role/update-role.component';
import { DemandeLecteurComponent } from './components/demande-lecteur/demande-lecteur.component';
import { CommandeLecteurComponent } from './components/commande-lecteur/commande-lecteur.component';
import { DetailDaLecteurComponent } from './components/detail-da-lecteur/detail-da-lecteur.component';
import { DetailBcLecteurComponent } from './components/detail-bc-lecteur/detail-bc-lecteur.component';
import { MenuComponent } from './components/menu/menu.component';
import { AddMenuComponent } from './components/add-menu/add-menu.component';
import { FactureComponent } from './components/facture/facture.component';
import { DetailFactureComponent } from './components/detail-facture/detail-facture.component';
import { DetailNotifFactureComponent } from './components/detail-notif-facture/detail-notif-facture.component';
import { UpdateMenuComponent } from './components/update-menu/update-menu.component';
import { FactureLecteurComponent } from './components/facture-lecteur/facture-lecteur.component';
import { DetailFcLecteurComponent } from './components/detail-fc-lecteur/detail-fc-lecteur.component';
import { FinalComponent } from './components/final/final.component';
import { AddFinalComponent } from './components/add-final/add-final.component';
import { UpdateFinalComponent } from './components/update-final/update-final.component';

const routes: Routes = [
  { path: 'details-da/:uid', component: DetailDemandeComponent },
  { path: 'details-bc/:uid', component: DetailCommandeComponent },
  { path: 'details-da-lecteur/:uid/:id_x3', component: DetailDaLecteurComponent },
  { path: 'details-bc-lecteur/:uid/:id_x3', component: DetailBcLecteurComponent },
  { path: 'details-fc-lecteur/:uid', component: DetailFcLecteurComponent },
  { path: 'login', component: LoginComponent },
  { path: 'users', component: UsersComponent},
  { path: 'roles', component: RolesComponent},
  { path: 'dash-admin', component: DashboardAdminComponent},
  { path: 'dash-val', component: DashboardValidateurComponent},
  { path: 'adduser', component: AddUserComponent},
  { path: 'addrole', component: AddRoleComponent},
  { path: 'addfinal', component: AddFinalComponent},
  { path: 'list-da', component: ListDemandeComponent},
  { path: 'list-bc', component: ListCommandeComponent},
  { path: 'lec-da', component: DemandeLecteurComponent},
  { path: 'lec-bc', component: CommandeLecteurComponent},
  { path: 'lec-fc', component: FactureLecteurComponent},
  { path: 'changePwd', component: ChangePwdComponent},
  { path: 'history', component: HistoryValidationComponent},
  { path: 'historyForUser', component: HistoryUserComponent},
  { path: 'updateuser/:id', component: UpdateUserComponent},
  { path: 'updaterole/:id', component: UpdateRoleComponent},
  { path: 'updatemenu/:id', component: UpdateMenuComponent},
  { path: 'updatefinal/:id', component: UpdateFinalComponent},
  { path: 'notif', component: NotificationComponent},
  { path: 'detail_notif/:uid', component: DetailNotifComponent},
  { path: 'detail_notif_facture/:uid', component: DetailNotifFactureComponent},
  { path: 'menu/:id', component: MenuComponent},
  { path: 'add-menu/:id', component: AddMenuComponent},
  { path: 'default', component: DefaultComponent},
  { path: 'facture', component: FactureComponent},
  { path: 'final', component: FinalComponent},
  { path: 'details-fac/:uid', component: DetailFactureComponent},
  { path: 'miss', component: MissingPwdComponent},
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' } 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
