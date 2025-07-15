package com.example.Bpa_v2_bakc.entities.data_dashboard;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataChart {
    private String mois;
    private int total_valid;
    private int total_refus; 
    private int total_part; 
}
