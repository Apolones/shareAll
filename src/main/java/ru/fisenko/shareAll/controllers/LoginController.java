package ru.fisenko.shareAll.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.fisenko.shareAll.models.Person;
import ru.fisenko.shareAll.services.PersonDetailsService;

@Controller
@RequestMapping("/auth")
public class LoginController {
    private PersonDetailsService personDetailsService;

    @Autowired
    public LoginController(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }


    @GetMapping("/login")
    String login() {
        return "/auth/login";
    }

    @GetMapping("/registration")
    String registration(@ModelAttribute("person") Person person) {return "/auth/registration";}

    @PostMapping("/registration")
    String newPerson(@ModelAttribute("person") Person person) {
        if (!personDetailsService.isUsernameUnique(person)) return "redirect:/auth/registration?error";
        else {
            personDetailsService.saveUser(person);
            return "/auth/login";
        }
    }

    @GetMapping("/user")
    public String userDetails(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username;
        if(userDetails!=null) username = userDetails.getUsername();
        else username = "User not authorized";
        model.addAttribute("login", username);
        return "/auth/user";
    }

}
