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

        model.addAttribute("textoTitulo", "Cadastro de Tipo de Item");
        model.addAttribute("textoSubTitulo", "Preencha as informações abaixo");
        model.addAttribute("cadastro", true);

        return "tipo-item/cadastro";
    }

    @PostMapping("/cadastro/salvar")
    public String salvarTipoItem(@Valid TipoItem tipoItem,
                                 BindingResult result,
                                 RedirectAttributes redirect,
                                 Model model,
                                 Authentication auth) {
        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        if (result.hasErrors()) {
            model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
            model.addAttribute("textoTitulo", "Cadastro de Tipo de Item");
            model.addAttribute("textoSubTitulo", "Preencha as informações abaixo");
            model.addAttribute("cadastro", tipoItem.getId() == null);  // true se for cadastro
            return "tipo-item/cadastro";
        }

        boolean sucesso = tipoItemService.cadastrarTipoItem(tipoItem);
        if (sucesso) {
            redirect.addFlashAttribute("mensagem", "Tipo de Item salvo com sucesso!");
            return "redirect:/tipo-item";  // Pode redirecionar para lista, por exemplo
        } else {
            // Nome duplicado
            model.addAttribute("mensagem", "Tipo de Item já existe.");
            model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
            model.addAttribute("textoTitulo", "Cadastro de Tipo de Item");
            model.addAttribute("textoSubTitulo", "Preencha as informações abaixo");
            model.addAttribute("cadastro", tipoItem.getId() == null);
            return "tipo-item/cadastro";
        }
    }


    @GetMapping("/editar/{id}")
    public String abrirFormularioEdicao(@PathVariable Long id, Authentication auth, Model model, RedirectAttributes redirect) {
        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        Optional<TipoItem> tipoItem = tipoItemService.buscarPorId(id);

        if (tipoItem.isPresent()) {
            model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
            model.addAttribute("tipoItem", tipoItem.get());
            model.addAttribute("textoTitulo", "Editar Tipo de Item");
            model.addAttribute("textoSubTitulo", "Atualize os dados do tipo de item.");
            model.addAttribute("cadastro", false);
            return "tipo-item/cadastro";
        } else {
            redirect.addFlashAttribute("mensagem", "Tipo de Item não encontrado.");
            return "redirect:/tipo-item";
        }
    }


    @PostMapping("/deletar/{id}")
    public String deletarTipoItem(@PathVariable Long id, RedirectAttributes redirect) {
        tipoItemService.deletarTipoItem(id);
        redirect.addFlashAttribute("mensagem", "Tipo de Item deletado com sucesso!");
        return "redirect:/tipo-item";
    }
}
