package com.example.Bpa_v2_bakc.entities.data_dashboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataAdmin {
    private int validLigne;
    private int refusLigne;
    private int validDA;
    private int refusDA;
    private int validBC;
    private int refusBC;
    private int validFC;
    private int refusFC;
    private int part_da;
    private int part_bc;
    private int part_fc;
    private int total_valid;
    private int total_refus;
    private int total_part;
    private int total;
}
