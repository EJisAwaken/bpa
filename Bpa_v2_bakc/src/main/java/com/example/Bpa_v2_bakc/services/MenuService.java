package com.example.Bpa_v2_bakc.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Bpa_v2_bakc.entities.mysql.Menu;
import com.example.Bpa_v2_bakc.repositories.mysql.MenuRepository;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;


    public List<Menu> findByRole(String idRole){
        List<Menu> menus = menuRepository.findByRoleAndActiveTrue(idRole);
        if (menus.isEmpty()) {
            return null;
        }
        return menus;
    }

    public Menu findByIdMenu(int idMenu){
        Optional<Menu> menuOptional = menuRepository.findById(idMenu);
        if(menuOptional.isPresent()){
            return menuOptional.get();
        }else{
            throw new RuntimeException("Menu not found with id: " + idMenu); 
        }
    }

    public void saveMenu(Menu menu){
        menuRepository.save(menu);
    }

    public void delete(int idMenu){
        Optional<Menu> menuOptional = menuRepository.findById(idMenu);
        if(menuOptional.isPresent()){
            Menu menu = menuOptional.get();
            menu.setActive(false);
            menuRepository.save(menu);
        }else{
            throw new RuntimeException("Menu not found with id: " + idMenu); 
        }
    }
    
    public void update(Menu menuUpadted){
        Optional<Menu> menuOptional = menuRepository.findById(menuUpadted.getIdMenu());
        if(menuOptional.isPresent()){
            Menu menu = menuOptional.get();
            menu.setRole(menuUpadted.getRole());
            menu.setLabel(menuUpadted.getLabel());
            menu.setIcone(menuUpadted.getIcone());
            menu.setRoute(menuUpadted.getRoute());
            menuRepository.save(menu);
        }else{
            throw new RuntimeException("Menu not found with id: " + menuUpadted.getIdMenu()); 
        }
    }
}
