package com.almoxarifado.controller;

import com.almoxarifado.entity.Usuario;
import com.almoxarifado.enums.Role;

import jakarta.validation.Valid;
import com.almoxarifado.security.UsuarioPrincipal;
import com.almoxarifado.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CadastroController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Authentication auth,
                                            Model model) {

        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        model.addAttribute("usuario", new Usuario());
        model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
        model.addAttribute("cadastro", true);
        model.addAttribute("roles", Role.values());

        return "autenticacao/cadastro";
    }


    @PostMapping("/cadastro/salvar")
    public String cadastrarUsuario(@Valid Usuario usuario,
                                   BindingResult result,
                                   RedirectAttributes redirect,
                                   Model model,
                                   Authentication auth) {
        if (result.hasErrors()) {
            if (usuario.getId() == null) {
                model.addAttribute("cadastro", true);

            } else {
                model.addAttribute("cadastro", false);

            }
            return "autenticacao/cadastro";
        }

        boolean sucesso;
        String mensagem;

        if (usuario.getId() == null) {
            sucesso = usuarioService.cadastrarUsuario(usuario);
            mensagem = sucesso ? "Usuário criado com sucesso!" : "Usuário já existe.";

            if (sucesso) {
                redirect.addFlashAttribute("sucesso", sucesso);
                redirect.addFlashAttribute("mensagem", mensagem);
                return "redirect:/cadastro"; // volta para tela de cadastro limpa
            }

            return "redirect:/cadastro";

        } else {
            UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
            sucesso = usuarioService.atualizarUsuario(principal.getUsuario().getId(), usuario);
            mensagem = sucesso ? "Usuário atualizado com sucesso!" : "Usuário já existe.";
            redirect.addFlashAttribute("sucesso", sucesso);
            redirect.addFlashAttribute("mensagem", mensagem);

            return "redirect:/editar-perfil"; // volta para edição
        }
    }

    @GetMapping("/editar-perfil")
    public String editarPerfil(Model model, Authentication auth) {
        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        model.addAttribute("usuario", usuario);
        model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
        model.addAttribute("cadastro", false);

        return "autenticacao/cadastro";
    }

}
