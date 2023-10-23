package ru.fisenko.shareAll.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.fisenko.shareAll.models.Person;
import ru.fisenko.shareAll.services.PersonDetailsService;
import ru.fisenko.shareAll.util.PersonValidator;

@Controller
@RequestMapping("/auth")
public class LoginController {
    private PersonDetailsService personDetailsService;
    private PersonValidator personValidator;

    @Autowired
    public LoginController(PersonDetailsService personDetailsService, PersonValidator personValidator) {
        this.personDetailsService = personDetailsService;
        this.personValidator = personValidator;
    }


    @GetMapping("/login")
    String login() {
        return "auth/login";
    }

    @GetMapping("/registration")
    String registration(@ModelAttribute("person") Person person) {return "auth/registration";}

    @PostMapping("/registration")
    String newPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) return "auth/registration";
        personDetailsService.saveUser(person);
        return "auth/login";
    }

    @GetMapping("user")
    public String userDetails(@AuthenticationPrincipal UserDetails userDetails, Model model, RedirectAttributes redirectAttributes) {
        String username = userDetails !=null ? userDetails.getUsername() : "User not authorized";
        model.addAttribute("login", username);
        return "auth/user";
    }
}
