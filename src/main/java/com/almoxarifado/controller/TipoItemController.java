package com.almoxarifado.controller;

import com.almoxarifado.entity.TipoItem;
import com.almoxarifado.entity.Usuario;
import com.almoxarifado.security.UsuarioPrincipal;
import com.almoxarifado.service.TipoItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/tipo-item")
public class TipoItemController {

    @Autowired
    private TipoItemService tipoItemService;

    @GetMapping("/")
    public String mostrarTiposItem(Authentication auth, Model model) {
        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
        model.addAttribute("listaTipoItens", tipoItemService.listarTiposItem());

        return "tipo-item/lista";
    }

    @GetMapping("/cadastro")
    public String mostrarFormularioTipoItem(Authentication auth, Model model) {
        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
        model.addAttribute("tipoItem", new TipoItem());
        model.addAttribute("cadastro", true);

        return "tipo-item/cadastro";
    }

    @PostMapping("/cadastro/salvar")
    public String salvarTipoItem(@Valid TipoItem tipoItem,
                                 BindingResult result,
                                 Model model,
                                 Authentication auth) {

        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        boolean cadastro = tipoItem.getId() == null;

        model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
        model.addAttribute("cadastro", cadastro);

        if (result.hasErrors()) {
            model.addAttribute("tipoItem", tipoItem); // mantém valores para corrigir
            return "tipo-item/cadastro";
        }

        boolean sucesso = tipoItemService.cadastrarTipoItem(tipoItem);

        model.addAttribute("sucesso", sucesso);
        model.addAttribute("mensagem", sucesso ? "Tipo de Item Salvo com Sucesso!" : "Tipo de Item já existe!");
        model.addAttribute("tipoItem", sucesso && cadastro ? new TipoItem() : tipoItem);

        return "tipo-item/cadastro";
    }



    @GetMapping("/editar/{id}")
    public String abrirFormularioEdicao(@PathVariable Long id, Authentication auth, Model model, RedirectAttributes redirect) {
        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        Optional<TipoItem> tipoItem = tipoItemService.buscarPorId(id);

        model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
        model.addAttribute("tipoItem", tipoItem.get());
        model.addAttribute("cadastro", false);

        return "tipo-item/cadastro";

    }


    @PostMapping("/deletar/{id}")
    public String deletarTipoItem(@PathVariable Long id, RedirectAttributes redirect) {
        tipoItemService.deletarTipoItem(id);
        redirect.addFlashAttribute("mensagem", "Tipo de Item deletado com sucesso!");

        return "redirect:/tipo-item/";
    }
}
