package com.example.Bpa_v2_bakc.repositories.jdbcTemplate;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.Bpa_v2_bakc.entities.data_dashboard.DataAdmin;
import com.example.Bpa_v2_bakc.entities.data_dashboard.DataAttente;
import com.example.Bpa_v2_bakc.entities.data_dashboard.DataChart;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StatistiqueRepository {
    private final JdbcTemplate jdbcTemplate;

    public StatistiqueRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DataAttente getAttenteForValidator(String idUser) {
        String sqlDa = """
            SELECT SUM(CASE WHEN etat = 1 THEN 1 ELSE 0 END) AS total_da 
            FROM demande WHERE id_x3 = ?
        """;
    
        String sqlBc = """
            SELECT SUM(CASE WHEN etat = 1 THEN 1 ELSE 0 END) AS total_bc 
            FROM commande WHERE id_x3 = ?
        """;
    
    
        Integer totalDa = jdbcTemplate.queryForObject(sqlDa, Integer.class,idUser);
        Integer totalBc = jdbcTemplate.queryForObject(sqlBc, Integer.class,idUser);
    
        DataAttente data = new DataAttente();
        data.setTotal_da(totalDa != null ? totalDa : 0);
        data.setTotal_bc(totalBc != null ? totalBc : 0);
        data.setTotal_fc( 0);
        data.setTotal(data.getTotal_da() + data.getTotal_bc() + data.getTotal_fc());
    
        return data;
    }

    public DataAdmin getDataForValidator(String date1, String date2,int idUser){
        String sql = """
            SELECT 
                SUM(CASE WHEN id_etat = 2 AND is_da = 3 THEN 1 ELSE 0 END) AS total_valid_ligne,
                SUM(CASE WHEN id_etat = 3 AND is_da = 3 THEN 1 ELSE 0 END) AS total_refus_ligne,
                SUM(CASE WHEN id_etat = 2 AND is_da = 1 THEN 1 ELSE 0 END) AS total_valid_DA,
                SUM(CASE WHEN id_etat = 3 AND is_da = 1 THEN 1 ELSE 0 END) AS total_refus_DA,
                SUM(CASE WHEN id_etat = 4 AND is_da = 1 THEN 1 ELSE 0 END) AS part_da,
                SUM(CASE WHEN id_etat = 2 AND is_da = 2 THEN 1 ELSE 0 END) AS total_valid_BC,
                SUM(CASE WHEN id_etat = 3 AND is_da = 2 THEN 1 ELSE 0 END) AS total_refus_BC,
                SUM(CASE WHEN id_etat = 4 AND is_da = 2 THEN 1 ELSE 0 END) AS part_bc,
                SUM(CASE WHEN id_etat = 2 AND is_da = 4 THEN 1 ELSE 0 END) AS total_valid_FC,
                SUM(CASE WHEN id_etat = 3 AND is_da = 4 THEN 1 ELSE 0 END) AS total_refus_FC,
                SUM(CASE WHEN id_etat = 3 AND is_da = 4 THEN 1 ELSE 0 END) AS part_fc,
                SUM(CASE WHEN id_etat =2 THEN 1 ELSE 0 END) AS total_valid,
                SUM(CASE WHEN id_etat =3 THEN 1 ELSE 0 END) AS total_refus,
                SUM(CASE WHEN id_etat =4 THEN 1 ELSE 0 END) AS total_part,
                COUNT(*) as total
            FROM validation 
            WHERE ? <= DATE(date_validation) AND DATE(date_validation) <= ? And id_user =?;
        """;

        return jdbcTemplate.queryForObject(sql, new Object[]{date1, date2,idUser}, new DataAdminMapper());
    }
    
    public DataAttente getAttenteForAdministrator() {
        String sqlDa = """
            SELECT SUM(CASE WHEN etat = 1 THEN 1 ELSE 0 END) AS total_da 
            FROM demande 
        """;
    
        String sqlBc = """
            SELECT SUM(CASE WHEN etat = 1 THEN 1 ELSE 0 END) AS total_bc 
            FROM commande 
        """;
    
        String sqlFc = """
            SELECT SUM(CASE WHEN etat = 1 THEN 1 ELSE 0 END) AS total_fc 
            FROM facturecompett
        """;
    
        Integer totalDa = jdbcTemplate.queryForObject(sqlDa, Integer.class);
        Integer totalBc = jdbcTemplate.queryForObject(sqlBc, Integer.class);
        Integer totalFc = jdbcTemplate.queryForObject(sqlFc, Integer.class);
    
        DataAttente data = new DataAttente();
        data.setTotal_da(totalDa != null ? totalDa : 0);
        data.setTotal_bc(totalBc != null ? totalBc : 0);
        data.setTotal_fc(totalFc != null ? totalFc : 0);
        data.setTotal(data.getTotal_da() + data.getTotal_bc() + data.getTotal_fc());
    
        return data;
    }

    
    public DataAdmin getDataForAdministrator(String date1, String date2){
        String sql = """
                SELECT 
                    SUM(CASE WHEN id_etat = 2 AND is_da = 3 THEN 1 ELSE 0 END) AS total_valid_ligne,
                    SUM(CASE WHEN id_etat = 3 AND is_da = 3 THEN 1 ELSE 0 END) AS total_refus_ligne,
                    SUM(CASE WHEN id_etat = 2 AND is_da = 1 THEN 1 ELSE 0 END) AS total_valid_DA,
                    SUM(CASE WHEN id_etat = 3 AND is_da = 1 THEN 1 ELSE 0 END) AS total_refus_DA,
                    SUM(CASE WHEN id_etat = 4 AND is_da = 1 THEN 1 ELSE 0 END) AS part_da,
                    SUM(CASE WHEN id_etat = 2 AND is_da = 2 THEN 1 ELSE 0 END) AS total_valid_BC,
                    SUM(CASE WHEN id_etat = 3 AND is_da = 2 THEN 1 ELSE 0 END) AS total_refus_BC,
                    SUM(CASE WHEN id_etat = 4 AND is_da = 2 THEN 1 ELSE 0 END) AS part_bc,
                    SUM(CASE WHEN id_etat = 2 AND is_da = 4 THEN 1 ELSE 0 END) AS total_valid_FC,
                    SUM(CASE WHEN id_etat = 3 AND is_da = 4 THEN 1 ELSE 0 END) AS total_refus_FC,
                    SUM(CASE WHEN id_etat = 3 AND is_da = 4 THEN 1 ELSE 0 END) AS part_fc,
                    SUM(CASE WHEN id_etat =2 THEN 1 ELSE 0 END) AS total_valid,
                    SUM(CASE WHEN id_etat =3 THEN 1 ELSE 0 END) AS total_refus,
                    SUM(CASE WHEN id_etat =4 THEN 1 ELSE 0 END) AS total_part,
                    COUNT(*) AS total
                FROM validation  
                WHERE ? <= DATE(date_validation) AND DATE(date_validation) <= ?;
                """;
        return jdbcTemplate.queryForObject(sql, new Object[]{date1, date2}, new DataAdminMapper());
    }

    public List<DataChart> getDataChartUser(String date1, String date2,int idUser){
        String sql = """
                SELECT 
                    DATE_FORMAT(date_validation, '%Y-%m') AS mois,
                    SUM(CASE WHEN id_etat = 2 THEN 1 ELSE 0 END) AS total_valid,
                    SUM(CASE WHEN id_etat = 3 THEN 1 ELSE 0 END) AS total_refus,
                    SUM(CASE WHEN id_etat = 4 THEN 1 ELSE 0 END) AS total_part
                FROM validation
                WHERE id_user=? and DATE(date_validation) BETWEEN ? AND ?
                GROUP BY DATE_FORMAT(date_validation, '%Y-%m')
                ORDER BY mois
                """;
        
        return jdbcTemplate.query(sql,new DataChartRowMapper(), new Object[]{idUser,date1, date2});
    }
    
    public List<DataChart> getDataChartAdmin(String date1, String date2){
        String sql = """
                SELECT 
                    DATE_FORMAT(date_validation, '%Y-%m') AS mois,
                    SUM(CASE WHEN id_etat = 2 THEN 1 ELSE 0 END) AS total_valid,
                    SUM(CASE WHEN id_etat = 3 THEN 1 ELSE 0 END) AS total_refus,
                    SUM(CASE WHEN id_etat = 4 THEN 1 ELSE 0 END) AS total_part
                FROM validation
                WHERE DATE(date_validation) BETWEEN ? AND ?
                GROUP BY DATE_FORMAT(date_validation, '%Y-%m')
                ORDER BY mois
                """;
        
        return jdbcTemplate.query(sql,new DataChartRowMapper(), new Object[]{date1, date2});
    }
}

class DataAttenteMapper implements RowMapper<DataAttente> {
    @Override
    public DataAttente mapRow(ResultSet rs, int rowNum) throws SQLException {
        DataAttente dashboard = new DataAttente();
        dashboard.setTotal_da(rs.getInt("total_da"));
        dashboard.setTotal_bc(rs.getInt("total_bc"));
        dashboard.setTotal_fc(rs.getInt("total_fc"));
        dashboard.setTotal(rs.getInt("total"));
        return dashboard;
    }
}

class DataAdminMapper implements RowMapper<DataAdmin> {
    @Override
    public DataAdmin mapRow(ResultSet rs, int rowNum) throws SQLException {
        DataAdmin dashboard = new DataAdmin();
        dashboard.setValidLigne(rs.getInt("total_valid_ligne"));
        dashboard.setRefusLigne(rs.getInt("total_refus_ligne"));
        dashboard.setValidDA(rs.getInt("total_valid_DA"));
        dashboard.setRefusDA(rs.getInt("total_refus_DA"));
        dashboard.setPart_da(rs.getInt("part_da"));
        dashboard.setValidBC(rs.getInt("total_valid_BC"));
        dashboard.setRefusBC(rs.getInt("total_refus_BC"));
        dashboard.setPart_bc(rs.getInt("part_bc"));
        dashboard.setValidFC(rs.getInt("total_valid_FC"));
        dashboard.setRefusFC(rs.getInt("total_refus_FC"));
        dashboard.setPart_fc(rs.getInt("part_fc"));
        dashboard.setTotal_valid(rs.getInt("total_valid"));
        dashboard.setTotal_refus(rs.getInt("total_refus"));
        dashboard.setTotal_part(rs.getInt("total_part"));
        dashboard.setTotal(rs.getInt("total"));
        return dashboard;
    }
}

class DataChartRowMapper implements RowMapper<DataChart> {
    @Override
    public DataChart mapRow(ResultSet rs, int rowNum) throws SQLException {
        DataChart dashboard = new DataChart();
        dashboard.setMois(rs.getString("mois"));
        dashboard.setTotal_valid(rs.getInt("total_valid"));
        dashboard.setTotal_refus(rs.getInt("total_refus"));
        dashboard.setTotal_part(rs.getInt("total_part"));
        return dashboard;
    }
}