package com.almoxarifado.controller;

import com.almoxarifado.entity.Item;
import com.almoxarifado.entity.Usuario;
import com.almoxarifado.enums.Role;
import com.almoxarifado.security.UsuarioPrincipal;
import com.almoxarifado.service.ArmarioService;
import com.almoxarifado.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/armario")
public class ArmarioController {

    @Autowired
    ArmarioService armarioService;

    @Autowired
    private EstoqueService estoqueService;

    @GetMapping("/")
    public String mostrarTodosEventosArmario(Authentication auth, Model model) {
        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
        model.addAttribute("listaEventos", armarioService.listarEventosArmario());

        return "armario/lista";
    }

    @GetMapping({"/abrir"})
    public String abrirArmario(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal, Model model) {
        this.armarioService.abrir(usuarioPrincipal.getUsuario());

        List<Item> itensAbaixoMinimo = this.estoqueService.buscarItensAbaixoEstoqueMinimo();

        model.addAttribute("nomeUsuario", usuarioPrincipal.getUsuario().primeiroNomeFormatado());
        model.addAttribute("aluno", usuarioPrincipal.getUsuario().getRole().equals(Role.ALUNO));
        model.addAttribute("itensAbaixoMinimo", itensAbaixoMinimo);
        model.addAttribute("mensagem", "Armário destrancado com Sucesso!");

        return "home";
    }

    @GetMapping({"/trancar"})
    public String trancarArmario(@AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal, Model model) {
        this.armarioService.trancar(usuarioPrincipal.getUsuario());

        List<Item> itensAbaixoMinimo = this.estoqueService.buscarItensAbaixoEstoqueMinimo();

        model.addAttribute("nomeUsuario", usuarioPrincipal.getUsuario().primeiroNomeFormatado());
        model.addAttribute("aluno", usuarioPrincipal.getUsuario().getRole().equals(Role.ALUNO));
        model.addAttribute("itensAbaixoMinimo", itensAbaixoMinimo);
        model.addAttribute("mensagem", "Armário trancado com Sucesso!");

        return "home";
    }
}
