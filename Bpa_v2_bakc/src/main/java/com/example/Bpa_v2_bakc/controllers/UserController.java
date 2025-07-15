package com.example.Bpa_v2_bakc.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.mysql.User;
import com.example.Bpa_v2_bakc.entities.object_request.UserRequest;
import com.example.Bpa_v2_bakc.services.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping()
    public Page<User> findAll(Pageable page,@RequestParam(required = false) String critere) {
        return userService.findAll(page, critere);
    }
    
    @GetMapping("/all")
    public List<User> findAllList() {
        return userService.findNotAmdin();
    }

    @GetMapping("/connected/{token}")
    public User getAllUtilisateurConnected(@PathVariable("token") String token) {
        return userService.getUserConnected(token);
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable int id) {
        return userService.findById(id);
    }

    @PostMapping()
    public void saveUser(@RequestBody UserRequest userRerquest) {
        userService.saveUser(userRerquest);
    }

    @PostMapping("/default")
    public void saveDefaultUser(@RequestBody UserRequest userRerquest) {
        userService.saveDefaultUser(userRerquest);
    }
    
    @PutMapping("/changerMotDePasse/{id}")
    public ResponseEntity<Boolean> changePassword(
            @PathVariable("id") int userId,
            @RequestParam(required = false) String old,
            @RequestParam(required = false) String news) {

        try {
            boolean isChanged = userService.changePassword(userId, old, news);
            return ResponseEntity.ok(isChanged); // ✅ Retourne un Boolean (true/false)
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false); // ✅ Retourne toujours un Boolean
        }
    }

    @PutMapping()
    public void updateUser(@RequestBody UserRequest userRerquest){
        userService.updateUser(userRerquest);
    }
   
    @PutMapping("/refreshPwd")
    public void refreshPwd(@RequestParam(required = false) String id_x3,@RequestParam(required = false) String newPwd){
        userService.refreshPwd(id_x3,newPwd);
    }

    @PutMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}
