package com.krylovichVI.sweater.service;

import com.krylovichVI.sweater.controller.ControllerUtils;
import com.krylovichVI.sweater.domain.Role;
import com.krylovichVI.sweater.domain.User;
import com.krylovichVI.sweater.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public boolean addUser(User user){
        User userFromDB = userRepo.findByUsername(user.getUsername());

        if(userFromDB != null){
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        sendMessage(user);

        return true;
    }

    private void sendMessage(User user) {
        if(!StringUtils.isEmpty(user.getEmail())){
            String message = String.format("Hello, %s! \n" +
                    "Welcom to Sweater. Please, visit next link: http://localhost:8080/activation/%s",
                    user.getUsername(),
                    user.getActivationCode());

           mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if(user == null){
            return false;
        }

        user.setActivationCode(null);
        userRepo.save(user);

        return true;
    }

    public boolean isActiveLink(String code){
        User user = userRepo.findByActivationCode(code);
        if(user == null){
            return false;
        } else{
            return true;
        }
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form){
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
        user.getRoles().clear();


        for(String key : form.keySet()){
            if(roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);
    }


    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();

        boolean isEmailChange = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));

        if(isEmailChange){
            user.setEmail(email);

            if(!StringUtils.isEmpty(email)){
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if(!StringUtils.isEmpty(password)){
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepo.save(user);

        if(isEmailChange){
            sendMessage(user);
        }

    }

    public void deleteUser(User user) {
        userRepo.delete(user);
    }

    public boolean consistUser(String email) {
        if(userRepo.findByEmail(email) != null){
            return true;
        }

        return false;
    }


    public void sendUserPassword(String email) {
        sendMessagePassword(userRepo.findByEmail(email));
    }

    public void sendMessagePassword(User user){
        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);

        String message = String.format(
                "You asked us to reset your forgotten password. To complete the process, please click on the link below or paste it into your browser: \n" +
                " http://localhost:8080/forgotpassword/%s/%s",
                user.getId(),
                user.getActivationCode()
        );

        mailSender.send(user.getEmail(), "Forgot password!", message);
    }

    public void updatePassword(String username, String password) {
        User user = userRepo.findByUsername(username);

        user.setPassword(passwordEncoder.encode(password));

        userRepo.save(user);
    }
    
}
