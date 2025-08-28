package com.almoxarifado.controller;

import com.almoxarifado.dto.MovimentacaoDto;
import com.almoxarifado.entity.Item;
import com.almoxarifado.entity.Movimentacao;
import com.almoxarifado.entity.Usuario;
import com.almoxarifado.enums.TipoMovimentacao;
import com.almoxarifado.security.UsuarioPrincipal;
import com.almoxarifado.service.EstoqueService;
import com.almoxarifado.service.MovimentacaoService;
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

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService movimentacaoService;

    @Autowired
    private EstoqueService estoqueService;

    @GetMapping("/")
    public String mostrarTodasMovimentacoes(Authentication auth, Model model) {
        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
        model.addAttribute("listaMovimentacoes", movimentacaoService.listarMovimentacoes());

        return "movimentacoes/lista";
    }

    @GetMapping("/nova/{tipo}")
    public String mostrarFormularioMovimentacao (@PathVariable TipoMovimentacao tipo,
                                                 Authentication auth,
                                                  Model model){

        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        List<Item> listaItens = estoqueService.listarItensAtivos();

        model.addAttribute("tipoSelecionado", tipo);
        model.addAttribute("tipos", TipoMovimentacao.values());
        model.addAttribute("listaItens", listaItens);
        model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());

        return "movimentacoes/cadastro";
    }

    @PostMapping("/nova/salvar")
    public String salvarItem(@Valid MovimentacaoDto dto,
                             BindingResult result,
                             RedirectAttributes redirect,
                             Model model,
                             Authentication auth) {

        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        Usuario usuario = principal.getUsuario();

        List<Item> listaItens = estoqueService.listarItensAtivos();

        model.addAttribute("nomeUsuario", usuario.primeiroNomeFormatado());
        model.addAttribute("listaItens", listaItens);
        model.addAttribute("tipos", TipoMovimentacao.values()); // enum para o select
        model.addAttribute("tipoSelecionado", dto.getTipo()); // pré-seleciona tipo

        if (result.hasErrors()) {
            model.addAttribute("movimentacao", dto); // mantém valores para corrigir
            return "movimentacoes/cadastro";
        }

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setItem(estoqueService.buscarPorId(dto.getItemId()).get());
        movimentacao.setTipo(dto.getTipo());
        movimentacao.setQuantidade(dto.getQuantidade());
        movimentacao.setObservacoes(dto.getObservacoes());
        movimentacao.setDataHora(LocalDateTime.now());
        movimentacao.setUsuario(usuario);

        boolean sucesso = movimentacaoService.registrarMovimentacao(movimentacao);

        model.addAttribute("sucesso", sucesso);
        model.addAttribute("mensagem", sucesso ? "Movimentação Salva com Sucesso!" : "Quantidade Insuficiente no estoque!");
        model.addAttribute("item", sucesso ? new MovimentacaoDto() : dto);

        return "movimentacoes/cadastro";
    }

}
