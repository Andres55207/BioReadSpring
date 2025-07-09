package com.example.BioRead.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Vista login.html
    }

    @GetMapping("/redirigir")
    public String redirigirSegunRol(Authentication auth) {
        if (auth == null) {
            return "redirect:/login";
        }

        // Log para debug
        System.out.println("Roles detectados: " + auth.getAuthorities());

        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            return "redirect:/usuario/dashboard";
        }

        return "redirect:/403"; // Error de rol no reconocido
    }

    @GetMapping("/403")
    public String accesoDenegado() {
        return "403"; // Vista 403.html
    }
}
