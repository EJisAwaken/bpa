package com.example.Bpa_v2_bakc.repositories.mysql;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.Bpa_v2_bakc.entities.mysql.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Integer>{
    @Query(value="SELECT * FROM notification WHERE id_x3=:code_dir AND etat=:etat ORDER BY id_notification DESC limit 15",nativeQuery = true)
    List<Notification> findByCode_dirAndEtat(String code_dir,int etat);

    @Query(value="SELECT * FROM notification WHERE id_x3=:id_x3 ORDER BY id_notification DESC limit 15",nativeQuery = true)
    List<Notification>  findByIdX3(String id_x3);

    @Query(value="SELECT * FROM notification WHERE id_x3=:id_x3 and recus is false",nativeQuery = true)
    List<Notification>  findById_X3AndRecusFlase(String id_x3);

    @Query(value="SELECT * FROM notification WHERE uid=:uid AND id_x3=:code_dir",nativeQuery = true)
    Optional<Notification> findByUidAndId_x3(String uid,String code_dir);
}
