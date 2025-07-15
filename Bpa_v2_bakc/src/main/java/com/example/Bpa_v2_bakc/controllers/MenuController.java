package com.example.Bpa_v2_bakc.controllers;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.mysql.Menu;
import com.example.Bpa_v2_bakc.services.MenuService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;





@RestController
@RequestMapping("/api/menu")
@CrossOrigin("*")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping()
    public ResponseEntity<List<Menu>> getByIdRole(@RequestParam String idRole) {
        List<Menu> menus = menuService.findByRole(idRole);
        return ResponseEntity.ok(menus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> getById(@PathVariable int id) {
        Menu menu = menuService.findByIdMenu(id);
        return ResponseEntity.ok(menu);
    }


    @PostMapping()
    public void saveMenu(@RequestBody Menu menu) {
        menuService.saveMenu(menu);
    }

    @PutMapping("/{id}")
    public void deleteMenu(@PathVariable int id) {
        menuService.delete(id);
    }

    @PutMapping()
    public void updateMenu(@RequestBody Menu menu) {
        menuService.update(menu);
    }
}
