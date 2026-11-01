
package com.example.onilecms.controller;

import com.example.onilecms.entity.UserRole;
import com.example.onilecms.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final RegistrationService registrationService;

    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String confirmPassword,
            @RequestParam(required = false) String role,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (fullName == null || fullName.isBlank()
                || email == null || email.isBlank()) {
            model.addAttribute("error",
                    "Full name and email are required.");
            return "register";
        }

        if (password == null || password.length() < 8) {
            model.addAttribute("error",
                    "Password must contain at least 8 characters.");
            return "register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error",
                    "Passwords do not match.");
            return "register";
        }

        UserRole selectedRole;

        if ("STUDENT".equalsIgnoreCase(role)) {
            selectedRole = UserRole.STUDENT;
        } else if ("INSTRUCTOR".equalsIgnoreCase(role)) {
            selectedRole = UserRole.INSTRUCTOR;
        } else {
            model.addAttribute("error",
                    "Please select Student or Instructor.");
            return "register";
        }

        try {
            registrationService.register(
                    fullName, email, password, selectedRole);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Registration successful. You can now log in.");

            return "redirect:/login";

        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }
}
