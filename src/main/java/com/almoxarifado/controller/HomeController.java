package com.almoxarifado.controller;

import com.almoxarifado.enums.Role;
import com.almoxarifado.security.UsuarioPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String getHome(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal, Model model) {
        model.addAttribute("nomeUsuario", usuarioPrincipal.getUsuario().primeiroNomeFormatado());
        model.addAttribute("aluno", usuarioPrincipal.getUsuario().getRole().equals(Role.ALUNO));
        return "home";
    }

}
