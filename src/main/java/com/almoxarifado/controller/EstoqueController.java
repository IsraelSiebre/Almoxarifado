package com.almoxarifado.controller;

import com.almoxarifado.entity.Item;
import com.almoxarifado.entity.TipoItem;
import com.almoxarifado.entity.Usuario;
import com.almoxarifado.security.UsuarioPrincipal;
import com.almoxarifado.service.EstoqueService;
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

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private TipoItemService tipoItemService;

    @GetMapping("/")
    public String mostarEstoque(Authentication auth, Model model) {
        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
        model.addAttribute("listaItens", estoqueService.listarItensAtivos());

        return "item/lista";
    }

    @GetMapping("/cadastro")
    public String mostrarFormularioItem(Authentication auth, Model model) {
        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        List<TipoItem> tiposDeItem = tipoItemService.listarTiposItem();

        model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
        model.addAttribute("item", new Item());
        model.addAttribute("tipos", tiposDeItem);

        model.addAttribute("textoTitulo", "Cadastro de Item");
        model.addAttribute("cadastro", true);

        return "item/cadastro";
    }

    @PostMapping("/cadastro/salvar")
    public String salvarItem(@Valid Item item,
                             BindingResult result,
                             RedirectAttributes redirect,
                             Model model,
                             Authentication auth) {

        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        List<TipoItem> tiposDeItem = tipoItemService.listarTiposItem();

        boolean cadastro = item.getId() == null;

        model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
        model.addAttribute("tipos", tiposDeItem);
        model.addAttribute("cadastro", cadastro);

        if (result.hasErrors()) {
            model.addAttribute("item", item); // mantém valores para corrigir
            return "item/cadastro";
        }

        boolean sucesso = estoqueService.cadastrarItem(item);

        model.addAttribute("sucesso", sucesso);
        model.addAttribute("mensagem", sucesso ? "Item Salvo com Sucesso!" : "Item já existe!");
        model.addAttribute("item", sucesso && cadastro ? new Item() : item);

        return "item/cadastro";
    }

    @GetMapping("/editar/{id}")
    public String abrirFormularioEdicao(@PathVariable Long id, Authentication auth, Model model, RedirectAttributes redirect) {
        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        List<TipoItem> tiposDeItem = tipoItemService.listarTiposItem();
        Optional<Item> item = estoqueService.buscarPorId(id);

        model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
        model.addAttribute("item", item.get());
        model.addAttribute("tipos", tiposDeItem);
        model.addAttribute("cadastro", false);

        return "item/cadastro";

    }

    @PostMapping("/inativar/{id}")
    public String deletarItem(@PathVariable Long id, RedirectAttributes redirect) {
        estoqueService.inativarItem(id);
        redirect.addFlashAttribute("mensagem", "Item Inativado com sucesso!");

        return "redirect:/estoque/";
    }

}
