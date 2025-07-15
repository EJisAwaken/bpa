import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DataAdmin } from 'src/app/models/dataAdmin';
import { DataAttente } from 'src/app/models/dataAttente';
import { StatistiqueService } from 'src/app/services/statistique.service';

@Component({
  selector: 'app-dashboard-admin',
  templateUrl: './dashboard-admin.component.html',
  styleUrls: ['./dashboard-admin.component.css']
})
export class DashboardAdminComponent implements OnInit {
    data: any = null;
    options: any;
    dataAdmin: DataAdmin | null = null;
    dataAttente: DataAttente | null = null;
    date1: string = "";
    date2: string = "";
    isLoading = false;
  
    constructor(
      private statistiqueService: StatistiqueService,
      private router: Router
    ) {}

    ngOnInit(): void {
        this.initializeChartOptions();
        this.toReload();
        this.getDataAttente();
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

    getDataDashboard(): void {
        this.isLoading = true;
        this.statistiqueService.getDataAdmin(this.date1, this.date2).subscribe({
            next: (result: DataAdmin) => {
                this.dataAdmin = result;
                this.isLoading = false;
                console.log("Données du dashboard :", result);
            },
            error: (err) => {
                console.error("Erreur lors de la récupération des données du dashboard:", err);
                this.isLoading = false;
            }
        });
    }

    getDataAttente(): void {
        this.isLoading = true;
        this.statistiqueService.getDataAttente().subscribe({
            next: (result: DataAttente) => {
                this.dataAttente = result;
                this.isLoading = false;
                console.log("Données du dashboard :", result);
            },
            error: (err) => {
                console.error("Erreur lors de la récupération des données du dashboard:", err);
                this.isLoading = false;
            }
        });
    }

    getDataChart(): void {
        this.isLoading = true;
        this.statistiqueService.getDataChartAdmin(this.date1, this.date2).subscribe({
            next: (data: any[]) => {
                this.data = this.prepareChartData(data);
                this.isLoading = false;
            },
            error: (err) => {
                console.error("Erreur lors de la récupération des données du graphique:", err);
                this.isLoading = false;
            }
        });
    }

    prepareChartData(data: any[]): any {
        return {
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
    }

    onFormSubmit(event: Event): void {
        event.preventDefault();
        if (this.isValidDateRange()) {
            this.getDataDashboard();
            this.getDataAttente();
            this.getDataChart();
        } else {
            alert("Veuillez entrer une plage de dates valide.");
        }
    }

    isValidDateRange(): boolean {
        const startDate = new Date(this.date1);
        const endDate = new Date(this.date2);
        return startDate <= endDate;
    }

    toReload(): void {
        const today = new Date();
        const yesterday = new Date(today);
        yesterday.setDate(today.getDate() - 1);

        this.date1 = yesterday.toISOString().split("T")[0];
        this.date2 = today.toISOString().split("T")[0];
        this.getDataDashboard();
        this.getDataChart();
    }

    redirectDA(): void {
        this.router.navigate(['lec-da']);
    }
    redirectBC(): void {
        this.router.navigate(['lec-bc']);
    }
    redirectFC(): void {
        this.router.navigate(['lec-fc']);
    }
}
