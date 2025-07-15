import { Component } from '@angular/core';
import { ActivatedRoute,Router } from '@angular/router';
import { Commande } from 'src/app/models/commande';
import { Demande } from 'src/app/models/demande';
import { Detail } from 'src/app/models/detail';
import { Detailbc } from 'src/app/models/detailbc';
import { Validation } from 'src/app/models/validation';
import { UserRequest } from 'src/app/object/userRequest';
import { CommandeService } from 'src/app/services/commande.service';
import { DemandeService } from 'src/app/services/demande.service';
import { DetailService } from 'src/app/services/detail.service';
import { DetailbcService } from 'src/app/services/detailbc.service';
import { UserService } from 'src/app/services/user.service';
import { ValidationService } from 'src/app/services/validation.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-detail-notif',
  templateUrl: './detail-notif.component.html',
  styleUrls: ['./detail-notif.component.css']
})
export class DetailNotifComponent {
  user: UserRequest | null = null;
  uid: string = "";
  motif: string = "";
  demande: Demande | null =null;
  commande: Commande | null =null;
  details: Detail[]=[];
  detailbcs: Detailbc[]=[];
  validation: Validation | null=null;
  isAprouve: boolean = false;
  isLoading: boolean = false;
  constructor(
    private route: ActivatedRoute,
    private validationService: ValidationService,
    private userService: UserService,
    private demandeService: DemandeService,
    private commandeService: CommandeService,
    private detailService: DetailService,
    private detailbcService: DetailbcService,
    private router: Router){}
  
  
  ngOnInit() {
    this.route.params.subscribe(params => {
      this.uid = params['uid'];
      if (this.uid) {
        this.getDemandeSelected();
        this.checkAprouve();
        this.getDetails();
        this.isLoading = false;
      }
    });
    this.getUserConnected();
  }

  getUserConnected(): void {
    this.userService.getUserConnected().subscribe(
      (resultat: UserRequest) => {
        this.user = resultat;
        this.getDemandeSelected();
        this.getDetails();
        this.isLoading = false;
      },
      (error) => {
        console.error("Erreur lors de la récupération de l'utilisateur connecté :", error);
      }
    );
  }


  getDemandeSelected(): void {
    if (!this.user) return;
  
    const { id_x3, interim } = this.user;
  
    this.demandeService.getByUidX3(this.uid, id_x3).subscribe((response) => {
      if (response) {
        this.assignDemande(response);
        this.checkAprouve();
      } else if (interim) {
        this.loadInterimDemande(interim);
      } else {
        this.loadCommande(id_x3, interim);
      }
    });
  }
  
  private loadInterimDemande(interim: string): void {
    this.demandeService.getByUidX3(this.uid, interim).subscribe((response) => {
      if (response) {
        this.assignDemande(response);
        this.checkAprouve();
      } else {
        this.loadCommande(this.user?.id_x3, interim);
      }
    });
  }
  
  private loadCommande(id_x3?: string, interim?: string): void {
    if (!id_x3) return;
  
    this.commandeService.findByUid(this.uid, id_x3).subscribe((data: Commande[]) => {
      if (data.length > 0) {
        this.commande = data[0];
        this.checkAprouve();
      } else if (interim) {
        this.loadInterimCommande(interim);
      }
    });
  }
  
  private loadInterimCommande(interim: string): void {
    this.commandeService.findByUid(this.uid, interim).subscribe((data: Commande[]) => {
      if (data.length > 0) {
        this.commande = data[0];
      }
      this.checkAprouve();
    });
  }

  private assignDemande(demande: any): void {
    this.demande = demande;
    this.isLoading = false;
  }
  
  

  getDetails(): void {
    if (!this.user) return;
  
    const { id_x3, interim } = this.user;
    this.isLoading = true;
  
    this.detailService.getByUid(this.uid, id_x3).subscribe({
      next: (data: Detail[]) => {
        if (data.length > 0) {
          this.assignDetails(data);
        } else if (interim) {
          this.loadInterimDetails(interim);
        } else {
          this.loadDetailBC();
        }
      },
      error: (error) => this.handleError('Erreur lors de la récupération des détails', error)
    });
  }
  
  private loadInterimDetails(interim: string): void {
    this.detailService.getByUid(this.uid, interim).subscribe({
      next: (data: Detail[]) => {
        if (data.length > 0) {
          this.assignDetails(data);
        } else {
          this.loadDetailBC();
        }
      },
      error: (error) => this.handleError('Erreur lors de la récupération des détails intérimaires', error)
    });
  }
  
  private loadDetailBC(): void {
    this.detailbcService.getByUid(this.uid).subscribe({
      next: (data: Detailbc[]) => {
        this.detailbcs = data;
        this.isLoading = false;
      },
      error: (error) => this.handleError('Erreur lors de la récupération des détails BC', error)
    });
  }
  
  private assignDetails(details: Detail[]): void {
    this.details = details;
    this.isLoading = false;
  }

  checkAprouve(): void {
    if (this.demande) {
      this.isAprouve = [2, 3, 4].includes(this.demande.etat?.idEtat);
    } else if (this.commande) {
      this.isAprouve = [2, 3, 4].includes(this.commande.etat?.idEtat);
    }
    this.isLoading = false;
  }

  toValid(uid: string): void {
    Swal.fire({
      title: 'Confirmer la validation',
      text: "Êtes-vous sûr de valider cette demande ?",
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Valider',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed && this.user) {
        this.isLoading = true;
  
        // Valeurs par défaut
        const valeur1 = 3;
        const valeur2 = 3;
        const valeur3 = 5;
        const idetat = 2;
        const designation = 'VAL';
  
        // Appel direct au service
        this.demandeService
          .validation(uid, this.user.id_x3, designation, valeur1, valeur2, valeur3,idetat,this.motif)
          .subscribe({
            next: () => {
              Swal.fire('Succès !', 'La demande a été validée avec succès.', 'success')
                .then(() => {
                  this.getDemandeSelected();
                  window.location.reload();
                });
              this.isLoading = false;
            },
            error: (error) => {
              this.handleError('Erreur lors de la validation', error);
              this.isLoading = false;
            }
          });
      }
    });
  }
  toValidArticle(uid: string,ref_dem: string,code_article:string): void {
    Swal.fire({
      title: 'Confirmer la validation',
      text: "Êtes-vous sûr de valider cette demande ?",
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Valider',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed && this.user) {
        this.isLoading = true;
  
        // Valeurs par défaut
        const valeur1 = 2;
        const valeur2 = 3;
        const valeur3 = 5;
        const idetat = 2;
        const designation = 'VAL';
  
        // Appel direct au service
        this.detailService
          .validation(uid,ref_dem,code_article, this.user.id_x3, designation, valeur1, valeur2, valeur3,idetat,this.motif)
          .subscribe({
            next: () => {
              Swal.fire('Succès !', 'La demande a été validée avec succès.', 'success')
                .then(() => {
                  this.getDemandeSelected();
                  this.getDetails();
                  window.location.reload(); // Ou remplacer par this.loadDemandes();
                });
              this.isLoading = false;
            },
            error: (error) => {
              this.handleError('Erreur lors de la validation', error);
              this.isLoading = false;
            }
          });
      }
    });
  }

  toRefuse(uid: string): void { 
    Swal.fire({
      title: 'Confirmer le refus',
      text: "Êtes-vous sûr de refuser cette demande ?",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Confirmer',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          title: 'Motif du refus',
          html:
            '<div class="form-group">' +
            '<textarea id="motif" class="form-control" rows="4" placeholder="Veuillez saisir le motif de refus..." required></textarea>' +
            '</div>',
          focusConfirm: false,
          showCancelButton: true,
          confirmButtonText: 'Envoyer',
          cancelButtonText: 'Annuler',
          preConfirm: () => {
            const motif = (document.getElementById('motif') as HTMLInputElement).value;
            if (!motif) {
              Swal.showValidationMessage('Veuillez saisir un motif de refus');
              return false;
            }
            return motif;
          }
        }).then((result) => {
          if (result.isConfirmed && this.user) {
            this.isLoading = true;
            this.motif = result.value;
            const valeur1 = 1;
            const valeur2 = 1;
            const valeur3 = 5;
            const idetat = 3;
            const designation = 'REJ';
      
            this.demandeService
              .validation(uid, this.user.id_x3, designation, valeur1, valeur2, valeur3,idetat,this.motif)
              .subscribe({
                next: () => {
                  Swal.fire('Succès !', 'La demande a été refusée avec succès.', 'success')
                    .then(() => {
                      window.location.reload(); 
                    });
                  this.isLoading = false;
                },
                error: (error) => {
                  this.handleError('Erreur lors de la refus', error);
                  this.isLoading = false;
                }
              });
          }
        });
      }
    });
  }

  toRefuseArticle(uid: string,ref_dem:string,code_article:string): void { 
    Swal.fire({
      title: 'Confirmer le refus',
      text: "Êtes-vous sûr de refuser cette demande ?",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Confirmer',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          title: 'Motif du refus',
          html:
            '<div class="form-group">' +
            '<textarea id="motif" class="form-control" rows="4" placeholder="Veuillez saisir le motif de refus..." required></textarea>' +
            '</div>',
          focusConfirm: false,
          showCancelButton: true,
          confirmButtonText: 'Envoyer',
          cancelButtonText: 'Annuler',
          preConfirm: () => {
            const motif = (document.getElementById('motif') as HTMLInputElement).value;
            if (!motif) {
              Swal.showValidationMessage('Veuillez saisir un motif de refus');
              return false;
            }
            return motif;
          }
        }).then((result) => {
          if (result.isConfirmed && this.user) {
            this.isLoading = true;
            this.motif = result.value;
            const valeur1 = 2;
            const valeur2 = 1;
            const valeur3 = 5;
            const idetat = 3;
            const designation = 'REJ';
      
            this.detailService
              .validation(uid,ref_dem,code_article, this.user.id_x3, designation, valeur1, valeur2, valeur3,idetat,this.motif)
              .subscribe({
                next: () => {
                  Swal.fire('Succès !', 'La demande a été refusée avec succès.', 'success')
                    .then(() => {
                      this.getDemandeSelected();
                      this.getDetails();
                      window.location.reload(); 
                    });
                  this.isLoading = false;
                },
                error: (error) => {
                  this.handleError('Erreur lors de la refus', error);
                  this.isLoading = false;
                }
              });
          }
        });
      }
    });
  }

  toValidbc(uid: string): void {
    Swal.fire({
      title: 'Confirmer la validation',
      text: "Êtes-vous sûr de valider cette commande ?",
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Valider',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed && this.user) {
        this.isLoading = true;
  
        // Valeurs par défaut
        const valeur1 = 3;
        const idetat = 2;
        const designation = 'VAL';
  
        // Appel direct au service
        this.commandeService
          .validation(uid, this.user.id_x3, designation, valeur1,idetat,this.motif)
          .subscribe({
            next: () => {
              Swal.fire('Succès !', 'La commande a été validée avec succès.', 'success')
                .then(() => {
                  this.getDemandeSelected();
                  window.location.reload();
                });
              this.isLoading = false;
            },
            error: (error) => {
              this.handleError('Erreur lors de la validation', error);
              this.isLoading = false;
            }
          });
      }
    });
  }

  toRefusebc(uid: string): void { 
    Swal.fire({
      title: 'Confirmer le refus',
      text: "Êtes-vous sûr de refuser cette commande ?",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Confirmer',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          title: 'Motif du refus',
          html:
            '<div class="form-group">' +
            '<textarea id="motif" class="form-control" rows="4" placeholder="Veuillez saisir le motif de refus..." required></textarea>' +
            '</div>',
          focusConfirm: false,
          showCancelButton: true,
          confirmButtonText: 'Envoyer',
          cancelButtonText: 'Annuler',
          preConfirm: () => {
            const motif = (document.getElementById('motif') as HTMLInputElement).value;
            if (!motif) {
              Swal.showValidationMessage('Veuillez saisir un motif de refus');
              return false;
            }
            return motif;
          }
        }).then((result) => {
          if (result.isConfirmed && this.user) {
            this.isLoading = true;
            this.motif = result.value;
            const valeur1 = 1;
            const idetat = 3;
            const designation = 'REJ';
      
            this.commandeService
              .validation(uid, this.user.id_x3, designation, valeur1,idetat,this.motif)
              .subscribe({
                next: () => {
                  Swal.fire('Succès !', 'La commande a été refusée avec succès.', 'success')
                    .then(() => {
                      this.getDemandeSelected();
                      window.location.reload(); 
                    });
                  this.isLoading = false;
                },
                error: (error) => {
                  this.handleError('Erreur lors de la refus', error);
                  this.isLoading = false;
                }
              });
          }
        });
      }
    });
  }

  
  private handleError(message: string, error: any): void {
    console.error(message, error);
    Swal.fire('Erreur', message, 'error');
  }

  redirectBack(): void{
    this.router.navigate(['/notif']);
  }

  ouvrirLien(lien : string): void {
    if(lien){
      window.open(lien , '_blank', 'noopener,noreferrer');
    }
  }
}
