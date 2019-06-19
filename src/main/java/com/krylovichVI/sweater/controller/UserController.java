package com.krylovichVI.sweater.controller;

import com.krylovichVI.sweater.domain.Role;
import com.krylovichVI.sweater.domain.User;
import com.krylovichVI.sweater.repos.UserRepo;
import com.krylovichVI.sweater.service.UserService;
import com.sun.javafx.sg.prism.NGShape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userList(Model model){
        model.addAttribute("users", userService.findAll());

        return "userList";
    }

    @GetMapping("{user}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userEditForm(
            @PathVariable() User user,
            Model model
    ){
        model.addAttribute("_user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping(params = "save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ){
        userService.saveUser(user, username, form);

        return "redirect:/user";
    }

    @PostMapping(params = "delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userDelete(
            @RequestParam("userId") User user
    ){
        userService.deleteUser(user);
        return "redirect:/user";
    }


    @GetMapping("profile")
    public String getProfile(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        return "profile";
    }

    @PostMapping("profile")
    public String updateUser(
            @RequestParam String password,
            @RequestParam String email,
            @AuthenticationPrincipal User user
    ){
        userService.updateProfile(user, password, email);

        return "redirect:/user/profile";
    }

    @GetMapping("subscribe/{user}")
    public String subscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
    ){
        userService.subscribe(currentUser, user);

        return "redirect:/user-messages/ " + user.getId();
    }

    @GetMapping("unsubscribe/{user}")
    public String unsubscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
    ){
        userService.unsubscribe(currentUser, user);

        return "redirect:/user-messages/" + user.getId();
    }

    @GetMapping("{type}/{user}/list")
    public String userList(
            Model model,
            @PathVariable String type,
            @PathVariable User user
    ){

        model.addAttribute("userChannel", user);
        model.addAttribute("type", type);

        if("ssubscriptions".equals(type)){
            model.addAttribute("users", user.getSubscriptions());
        }else{
            model.addAttribute("users", user.getSubscribers());
        }

        return "subscriptions";
    }

}
