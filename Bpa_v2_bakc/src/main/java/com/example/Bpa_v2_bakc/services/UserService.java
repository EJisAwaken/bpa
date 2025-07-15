package com.example.Bpa_v2_bakc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Bpa_v2_bakc.entities.mysql.Role;
import com.example.Bpa_v2_bakc.entities.mysql.User;
import com.example.Bpa_v2_bakc.entities.object_request.UserRequest;
import com.example.Bpa_v2_bakc.repositories.mysql.RoleRepository;
import com.example.Bpa_v2_bakc.repositories.mysql.UserRepository;
import com.example.Bpa_v2_bakc.util.JwtUtil;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;


    public Page<User> findAll(Pageable page,String critere){
        return userRepository.findAll(page, "%"+critere+"%");
    }
    
    public void saveDefaultUser(UserRequest userRequest) {
        User user = new User();
        String hashedPassword = passwordEncoder.encode(userRequest.getMot_de_passe());
        Role role = roleService.findByRole(1);
        user.setNom(userRequest.getNom());
        user.setPrenom(userRequest.getPrenom());
        user.setEmail(userRequest.getEmail());
        user.setMot_de_passe(hashedPassword);
        user.setRole(role);
        user.setId_x3(userRequest.getId_x3());
        user.setInterim(userRequest.getInterim());
        userRepository.save(user);
    }

    public void saveUser(UserRequest userRequest) {
        User user = new User();
        String hashedPassword = passwordEncoder.encode(userRequest.getMot_de_passe());
        Role role= roleService.findByRole(userRequest.getIdRole());

        user.setNom(userRequest.getNom());
        user.setPrenom(userRequest.getPrenom());
        user.setEmail(userRequest.getEmail());
        user.setMot_de_passe(hashedPassword);
        user.setId_x3(userRequest.getId_x3());
        user.setInterim(userRequest.getInterim());
        user.setRole(role);
        userRepository.save(user);
        
        String subject = "Information de création de compte - Easy Approve BPA";

        String content = "<!DOCTYPE html>"
            + "<html lang='fr'>"
            + "<head>"
            + "<meta charset='UTF-8'>"
            + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
            + "<title>Creation de compte</title>"
            + "<style>"
            + "  body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; }"
            + "  .container { max-width: 600px; margin: 20px auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; }"
            + "  .header { background-color: #2c3e50; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; }"
            + "  .content { padding: 20px; }"
            + "  .footer { text-align: center; padding: 20px; font-size: 12px; color: #777; }"
            + "  .button { background-color: #3498db; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block; }"
            + "  .credentials { background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0; }"
            + "  .logo { max-width: 150px; margin-bottom: 20px; }"
            + "</style>"
            + "</head>"
            + "<body>"
            + "<div class='container'>"
            + "  <div class='header'>"
            + "    <h1>Bienvenue sur Easy Approve BPA</h1>"
            + "  </div>"
            + "  <div class='content'>"
            + "    <p>Bonjour " + user.getPrenom() + ",</p>"
            + "    <p>Votre compte " + role.getRole() + " a été créé avec succès sur notre plateforme d'approbation de BPA.</p>"
            + "    <p>Voici vos informations de connexion :</p>"
            + "    <div class='credentials'>"
            + "      <p><strong>Email :</strong> " + user.getEmail() + "</p>"
            + "      <p><strong>Mot de passe temporaire :</strong> Default@25?</p>"
            + "    </div>"
            + "    <p style='color: #e74c3c;'><strong>Pour des raisons de sécurité, nous vous recommandons de changer votre mot de passe dès votre première connexion.</strong></p>"
            + "    <p>Pour accéder à la page de login, veuillez cliquer sur le bouton ci-dessous :</p>"
            + "    <p style='text-align: center; margin: 30px 0;'><a href='http://192.168.122.2:4201' class='button'>Accéder à l'application</a></p>"
            + "    <p>Si vous rencontrez des difficultés ou si vous n'avez pas demandé cette création de compte, veuillez contacter immédiatement notre support technique.</p>"
            + "    <p>Cordialement,</p>"
            + "    <p><strong>L'équipe BPA</strong></p>"
            + "  </div>"
            + "  <div class='footer'>"
            + "    <p>© 2025 Plateforme d'approbation de BPA. Tous droits réservés.</p>"
            + "    <p>Cet e-mail a été envoyé automatiquement, merci de ne pas y répondre.</p>"
            + "  </div>"
            + "</div>"
            + "</body>"
            + "</html>";

        emailService.sendEmail(user.getEmail(), subject, content);
    }

    public User findById(int id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            return userOptional.get();
        }else{
            throw new RuntimeException("Rôle introuvable avec l'ID : " + id);
        }
    }
    
    public User  getUserConnected(String token){
        User user = JwtUtil.getUser(token);
        return user;
    }

    public void deleteUser(int idUser){
        Optional<User> userOptional = userRepository.findById(idUser);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setActive(false);
            userRepository.save(user);
        }else{
            throw new RuntimeException("Utilisateur not found with id: " + idUser); 
        }
    }

    public void updateUser(UserRequest userRequest){
        Optional<User> userOptional = userRepository.findById(userRequest.getIdUser());
        
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Utilisateur not found with id: " + userRequest.getIdUser());
        }
    
        User user = userOptional.get();
        user.setNom(userRequest.getNom());
        user.setPrenom(userRequest.getPrenom());
        user.setEmail(userRequest.getEmail());
        user.setId_x3(userRequest.getId_x3());
        user.setInterim(userRequest.getInterim());
    
        Optional<Role> roleOptional = roleRepository.findById(userRequest.getIdRole());
        roleOptional.ifPresent(user::setRole);
    
        userRepository.save(user);
    }

    public List<User> findNotAmdin(){
        List<User> users = userRepository.findAll();
        return users;
    }

    public User getUserById(int id){
        User utilisateur = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return utilisateur;
    }
    
    public User getUserByIdX3(String idX3){
        return userRepository.findByIdx3(idX3)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
    
    public Optional<User> getUserByIdX3Opt(String idX3){
        return userRepository.findByIdx3(idX3);
    }

    public void refreshPwd(String id_x3,String newPwd){
        Optional<User> userOp = userRepository.findByIdx3(id_x3);
        if (userOp.isPresent()) {
            User user = userOp.get();
            String hashedPassword = passwordEncoder.encode(newPwd);
            String subject = "Reinitialisation de mot de passe - Easy Approve BPA";

            String content = "<!DOCTYPE html>"
                + "<html lang='fr'>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<title>Reinitialisation de mot de passe</title>"
                + "<style>"
                + "  body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; }"
                + "  .container { max-width: 600px; margin: 20px auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; }"
                + "  .header { background-color: #2c3e50; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; }"
                + "  .content { padding: 20px; }"
                + "  .footer { text-align: center; padding: 20px; font-size: 12px; color: #777; }"
                + "  .button { background-color: #3498db; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block; }"
                + "  .credentials { background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0; }"
                + "  .logo { max-width: 150px; margin-bottom: 20px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "  <div class='header'>"
                + "    <h1>Bienvenue sur la plateforme BPA</h1>"
                + "  </div>"
                + "  <div class='content'>"
                + "    <p>Bonjour " + user.getPrenom() + ",</p>"
                + "    <p>Votre mot de passe sur Easy Approve a été reinitialise par l'administrateur de l'application.</p>"
                + "    <p>Voici le mot de passe reinitialiser :</p>"
                + "    <div class='credentials'>"
                + "      <p><strong>Mot de passe temporaire :</strong>"+newPwd+"</p>"
                + "    </div>"
                + "    <p style='color: #e74c3c;'><strong>Pour des raisons de sécurité, nous vous recommandons de changer votre mot de passe dès que vous etes connecte.</strong></p>"
                + "    <p style='color: #e74c3c;'><strong>NB : si vous n'avez pas fait une demande de réinitialisation de mot de passe, veuillez contacter l'aministrateur de l'application</strong></p>"
                + "    <p>Cordialement,</p>"
                + "    <p><strong>L'équipe BPA</strong></p>"
                + "  </div>"
                + "  <div class='footer'>"
                + "    <p>© 2025 Plateforme d'approbation de BPA. Tous droits réservés.</p>"
                + "    <p>Cet e-mail a été envoyé automatiquement, merci de ne pas y répondre.</p>"
                + "  </div>"
                + "</div>"
                + "</body>"
                + "</html>";
            user.setMot_de_passe(hashedPassword);
            userRepository.save(user);
            emailService.sendEmail(user.getEmail(), subject, content);
        }else{
            throw new RuntimeException("Le nouveau mot de passe doit être différent de l'ancien");
        }
    }

    @Transactional
    public boolean changePassword(int userId, String currentPassword, String newPassword) {
        User utilisateur = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (currentPassword == null || newPassword == null || newPassword.trim().isEmpty()) {
            throw new RuntimeException("Les mots de passe ne peuvent pas être vides");
        }

        if (!passwordEncoder.matches(currentPassword, utilisateur.getMot_de_passe())) {
            throw new RuntimeException("Mot de passe actuel incorrect");
        }

        if (currentPassword.equals(newPassword)) {
            throw new RuntimeException("Le nouveau mot de passe doit être différent de l'ancien");
        }

        utilisateur.setMot_de_passe(passwordEncoder.encode(newPassword));
        userRepository.save(utilisateur);
        return true;
    }

}
