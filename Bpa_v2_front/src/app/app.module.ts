import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SidebarComponent } from './component/sidebar/sidebar.component';
import { TopbarComponent } from './component/topbar/topbar.component';
import { MainComponent } from './component/main/main.component';
import { DetailDemandeComponent } from './components/detail-demande/detail-demande.component';
import { LoginComponent } from './components/login/login.component';
import { PaginatorModule } from 'primeng/paginator';
import { NumberFormatPipe } from './number-format.pipe';
import { HttpClientModule } from '@angular/common/http';
import { UsersComponent } from './components/users/users.component';
import { AddUserComponent } from './components/add-user/add-user.component';
import { UpdateUserComponent } from './components/update-user/update-user.component';
import { ChangePwdComponent } from './components/change-pwd/change-pwd.component';
import { MissingPwdComponent } from './components/missing-pwd/missing-pwd.component';
import { DefaultComponent } from './components/default/default.component';
import {ChartModule} from 'primeng/chart';
import { HistoryValidationComponent } from './components/history-validation/history-validation.component';
import { HistoryUserComponent } from './components/history-user/history-user.component';
import { ListDemandeComponent } from './components/list-demande/list-demande.component';
import { NotificationComponent } from './components/notification/notification.component';
import { DetailNotifComponent } from './components/detail-notif/detail-notif.component';
import { DashboardAdminComponent } from './components/dashboard-admin/dashboard-admin.component';
import { DashboardValidateurComponent } from './components/dashboard-validateur/dashboard-validateur.component';
import { DetailCommandeComponent } from './components/detail-commande/detail-commande.component';
import { ListCommandeComponent } from './components/list-commande/list-commande.component';
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

@NgModule({
  declarations: [
    AppComponent,
    SidebarComponent,
    TopbarComponent,
    MainComponent,
    DetailDemandeComponent,
    LoginComponent,
    NumberFormatPipe,
    UsersComponent,
    AddUserComponent,
    UpdateUserComponent,
    ChangePwdComponent,
    MissingPwdComponent,
    DefaultComponent,
    HistoryValidationComponent,
    NotificationComponent,
    HistoryUserComponent,
    ListDemandeComponent,
    DetailNotifComponent,
    DashboardAdminComponent,
    DashboardValidateurComponent,
    DetailCommandeComponent,
    ListCommandeComponent,
    RolesComponent,
    AddRoleComponent,
    UpdateRoleComponent,
    DemandeLecteurComponent,
    CommandeLecteurComponent,
    DetailDaLecteurComponent,
    DetailBcLecteurComponent,
    MenuComponent,
    AddMenuComponent,
    FactureComponent,
    DetailFactureComponent,
    DetailNotifFactureComponent,
    UpdateMenuComponent,
    FactureLecteurComponent,
    DetailFcLecteurComponent,
    FinalComponent,
    AddFinalComponent,
    UpdateFinalComponent  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    PaginatorModule,
    HttpClientModule,
    ChartModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
