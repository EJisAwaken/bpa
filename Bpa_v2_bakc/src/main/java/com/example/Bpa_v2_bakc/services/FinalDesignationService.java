package com.example.Bpa_v2_bakc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Bpa_v2_bakc.entities.mysql.Finaldestination;
import com.example.Bpa_v2_bakc.repositories.mysql.FinalDestinationRepository;

@Service
public class FinalDesignationService {
    private final FinalDestinationRepository finalDestinationRepository;

    private FinalDesignationService(
        FinalDestinationRepository finalDestinationRepository
    ){
        this.finalDestinationRepository = finalDestinationRepository;
    }
    
    public List<Finaldestination> getAllFinaldestinations(){
        return finalDestinationRepository.findByActiveTrue();
    }

    public Finaldestination getByid(int id){
        Optional<Finaldestination> fOptional = finalDestinationRepository.findById(id);
        if(fOptional.isPresent()){
            return fOptional.get();
        }else{
            throw new RuntimeException("Role not found with id: " + id); 
        }
    }
    
    public Finaldestination getByUid(String uid){
        Optional<Finaldestination> fOptional = finalDestinationRepository.findByUidAndActiveTrue(uid);
        if(fOptional.isPresent()){
            return fOptional.get();
        }else{
            throw new RuntimeException("Role not found with id: " + uid); 
        }
    }

    public void insertFinalDestination(Finaldestination finaldestination){
        Finaldestination des = new Finaldestination();
        des.setUid(finaldestination.getUid());
        des.setDesignation(finaldestination.getDesignation());
        finalDestinationRepository.save(des);
    }

    public void deleteFinalDestination(int id){
        Optional<Finaldestination> deOptional = finalDestinationRepository.findById(id);
        if (deOptional.isPresent()) {
            Finaldestination finaldestination =deOptional.get();
            finaldestination.setActive(false);
            finalDestinationRepository.save(finaldestination);
        }else{
            System.out.println("final destination introuvable : " + id);
        }
    }

    public void updateFinalDestination(Finaldestination finalUpdate){
        Optional<Finaldestination> deOptional = finalDestinationRepository.findById(finalUpdate.getIdFinal());
        if (deOptional.isPresent()){
            Finaldestination finaldestination = deOptional.get();
            finaldestination.setUid(finalUpdate.getUid());
            finaldestination.setDesignation(finalUpdate.getDesignation());
            finalDestinationRepository.save(finaldestination);
        }else{
            System.out.println("final destination introuvable : " + finalUpdate.getIdFinal());
        }
    }
}
