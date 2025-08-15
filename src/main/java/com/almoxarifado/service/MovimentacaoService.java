package com.almoxarifado.service;

import com.almoxarifado.entity.Movimentacao;
import com.almoxarifado.repository.MovimentacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private EstoqueService estoqueService;

    @Transactional
    public boolean registrarMovimentacao(Movimentacao movimentacao) {

        switch (movimentacao.getTipo()) {
            case ENTRADA ->
                    estoqueService.aumentarQuantidadeEmEstoque(
                            movimentacao.getItem(), movimentacao.getQuantidade()
                    );

            case SAIDA -> {
                if (movimentacao.getItem().getQuantidade() < movimentacao.getQuantidade()) {
                    return false; // Estoque insuficiente
                }
                estoqueService.diminuirQuantidadeEmEstoque(
                        movimentacao.getItem(), movimentacao.getQuantidade()
                );
            }
        }

        movimentacaoRepository.save(movimentacao);
        return true;
    }

    public List<Movimentacao> listarMovimentacoes() {
        return movimentacaoRepository.findAll();
    }

    public void deletarMovimentacao(Long id) {
        movimentacaoRepository.deleteById(id);
    }

}
