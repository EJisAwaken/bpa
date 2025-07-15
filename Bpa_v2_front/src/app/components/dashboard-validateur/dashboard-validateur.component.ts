import { Component, OnInit } from '@angular/core';
import { DataAdmin } from 'src/app/models/dataAdmin';
import { DataAttente } from 'src/app/models/dataAttente';
import { UserRequest } from 'src/app/object/userRequest';
import { StatistiqueService } from 'src/app/services/statistique.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-dashboard-validateur',
  templateUrl: './dashboard-validateur.component.html',
  styleUrls: ['./dashboard-validateur.component.css']
})
export class DashboardValidateurComponent implements OnInit {
  data: any;
  options: any;
  dataVal: DataAdmin | null = null;
  dataAttente: DataAttente | null = null;
  date1: string = "";
  date2: string = "";
  user: UserRequest | null = null;
  isLoading = false;

  constructor(
    private userService: UserService,
    private statistiqueService: StatistiqueService
  ) {}

  ngOnInit(): void {
    this.initializeChartOptions();
    this.toReload();
  }

  initializeChartOptions() {
    this.options = {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'top'
        }
      },
      scales: {
        x: {
          title: {
            display: true,
            text: 'Mois'
          }
        },
        y: {
          title: {
            display: true,
            text: 'Compte total'
          }
        }
      }
    };
  }

  getUserConnected(): void {
    this.isLoading = true;
    this.userService.getUserConnected().subscribe({
      next: (resultat: UserRequest) => {
        this.user = resultat;
        this.getDataDashboard();
        this.getDataChart();
        this.getDataAttente();
      },
      error: (error) => {
        console.error("Erreur lors de la récupération de l'utilisateur connecté :", error);
        this.isLoading = false;
      }
    });
  }

  getDataDashboard(): void {
    if (!this.user) return;

    this.statistiqueService.getDataVal(this.date1, this.date2, this.user.idUser).subscribe({
      next: (result: DataAdmin) => {
        this.dataVal = result;
        this.isLoading = false;
      },
      error: (err) => {
        console.error("Erreur lors de la récupération des données du tableau de bord :", err);
        this.isLoading = false;
      }
    });
  }

  getDataAttente(): void {
    if (!this.user) return;

    this.statistiqueService.getDataAttenteVal(this.user.id_x3).subscribe({
      next: (result: DataAttente) => {
        this.dataAttente = result;
        this.isLoading = false;
        console.log("resultat :",result);
      },
      error: (err) => {
        console.error("Erreur lors de la récupération des données du tableau de bord :", err);
        this.isLoading = false;
      }
    });
  }

  getDataChart(): void {
    if (!this.user) return;

    this.statistiqueService.getDataChartUser(this.date1, this.date2, this.user.idUser).subscribe({
      next: (data: any[]) => {
        this.data = {
          labels: data.map(item => item.mois),
          datasets: [
            {
              label: "Total Validé",
              backgroundColor: "#1CC88A",
              data: data.map(item => item.total_valid)
            },
            {
              label: "Total Refusé",
              backgroundColor: "#E74A3B",
              data: data.map(item => item.total_refus)
            },
            {
              label: "Total Partiellement Validé",
              backgroundColor: "#0032A0",
              data: data.map(item => item.total_part)
            }
          ]
        };
        this.isLoading = false;
      },
      error: (err) => {
        console.error("Erreur lors de la récupération des données du graphique :", err);
        this.isLoading = false;
      }
    });
  }

  onFormSubmit(event: Event): void {
    event.preventDefault();
    if (this.date1 && this.date2) {
      this.getDataDashboard();
      this.getDataChart();
    }
  }

  toReload(): void {
    const today = new Date();
    const yesterday = new Date(today);
    yesterday.setDate(today.getDate() - 1);

    this.date1 = yesterday.toISOString().split("T")[0];
    this.date2 = today.toISOString().split("T")[0];
    this.getUserConnected();
  }
}
