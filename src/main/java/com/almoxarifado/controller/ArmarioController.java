package com.almoxarifado.controller;

import com.almoxarifado.entity.Usuario;
import com.almoxarifado.security.UsuarioPrincipal;
import com.almoxarifado.service.ArmarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/armario")
public class ArmarioController {

    @Autowired
    ArmarioService armarioService;

    @GetMapping("/")
    public String mostrarTodosEventosArmario(Authentication auth, Model model) {
        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
        model.addAttribute("listaEventos", armarioService.listarEventosArmario());

        return "armario/lista";
    }

    @PostMapping("/armario/abrir")
    @ResponseBody
    public String abrirArmario(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal) {
        armarioService.abrir(usuarioPrincipal.getUsuario());

        System.out.println("armario aberto");

        return "Armário destrancado com sucesso!";
    }

    @PostMapping("/armario/trancar")
    @ResponseBody
    public String trancarArmario(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal) {
        armarioService.trancar(usuarioPrincipal.getUsuario());


        System.out.println("armario trancado");

        return "Armário trancado com sucesso!";
    }



}
