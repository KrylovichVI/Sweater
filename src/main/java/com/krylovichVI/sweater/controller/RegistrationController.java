package com.krylovichVI.sweater.controller;

import com.krylovichVI.sweater.domain.User;
import com.krylovichVI.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;


    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }


    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String passwordConfirm,
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ){
        if(StringUtils.isEmpty(passwordConfirm)){
            model.addAttribute("password2Error", "Password confirmation cannot be empty");
        }

        boolean isConfirmEmpty = user.getPassword() != null && !user.getPassword().equals(passwordConfirm);
        if(isConfirmEmpty){
            model.addAttribute("passwordError", "Passwords are different!");
        }

        if(isConfirmEmpty || bindingResult.hasErrors()){
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);
            return "registration";
        }

        if(userService.consistUser(user.getEmail())){
            model.addAttribute("emailError", "Email is used");
            return "registration";
        }

        if(!userService.addUser(user)){
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }


    @GetMapping("/activation/{code}")
    public String activationCode(Model model, @PathVariable String code){
        boolean isActivated = userService.activateUser(code);
        if(isActivated){
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        }else{
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found!");
        }
        return "login";
    }

    @GetMapping("/forgotpassword")
    public String forgotPassword(){
        return "passwordForgot";
    }

    @PostMapping("/forgotpassword")
    public String returnPassword(
            @RequestParam String email,
            Model model
    ){
        boolean isConsist = userService.consistUser(email);

        if(isConsist){
            userService.sendUserPassword(email);
            return "redirect:/forgotpassword/reset";
        }else{
            model.addAttribute("message", "There is no user under this email.");
            return "redirect:/forgotpassword";
        }
    }

    @GetMapping("/forgotpassword/reset" )
    public String resetMail(){
        return "resetMail";
    }

    @GetMapping("/forgotpassword/{user}/{code}")
    public String reset(
            @PathVariable User user,
            @PathVariable String code,
            Model model
    ){
        boolean isActive = userService.activateUser(code);

        if(isActive){
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Successfully activated");
        }else{
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation link is not found!");
        }
        return "resetPassword";
    }

    @PostMapping("/forgotpassword/{user}/{code}")
    public String resetPassword(
            @PathVariable User user,
            @PathVariable String code,
            @RequestParam String password,
            Model model
    ){

        if(StringUtils.isEmpty(password)){
            model.addAttribute("message", "Password confirmation cannot be empty");
            return "redirect:/forgotpassword/{user}/{code}";
        }


        userService.updatePassword(user.getUsername(), password);
        return "redirect:/login";
    }
}
