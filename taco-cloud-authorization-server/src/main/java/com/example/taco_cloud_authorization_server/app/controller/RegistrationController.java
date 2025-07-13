package com.example.taco_cloud_authorization_server.app.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.taco_cloud_authorization_server.app.common.Urls;
import com.example.taco_cloud_authorization_server.app.configuration.security.RegistrationForm;
import com.example.taco_cloud_authorization_server.app.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;


    @GetMapping
    public String getRegister(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return Urls.REGISTER.get();
    }

    @PostMapping
    public String postRegister(@ModelAttribute RegistrationForm form, Model model) {
        if (this.userRepo.findByUsername(form.getUsername()) != null) {
            model.addAttribute("error", "User already exists!");
            return Urls.REGISTER.get();
        }
        
        this.userRepo.save(form.toUser(this.encoder));
        return "redirect:/" + Urls.LOGIN.get();
    }
}
