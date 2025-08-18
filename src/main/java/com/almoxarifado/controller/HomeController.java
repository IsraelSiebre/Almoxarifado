package com.almoxarifado.controller;

import com.almoxarifado.entity.Item;
import com.almoxarifado.enums.Role;
import com.almoxarifado.security.UsuarioPrincipal;
import com.almoxarifado.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private EstoqueService estoqueService;

    @GetMapping("/home")
    public String getHome(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal, Model model) {
        List<Item> itensAbaixoMinimo = estoqueService.buscarItensAbaixoEstoqueMinimo();

        model.addAttribute("nomeUsuario", usuarioPrincipal.getUsuario().primeiroNomeFormatado());
        model.addAttribute("aluno", usuarioPrincipal.getUsuario().getRole().equals(Role.ALUNO));
        model.addAttribute("itensAbaixoMinimo", itensAbaixoMinimo);

        return "home";
    }

}
