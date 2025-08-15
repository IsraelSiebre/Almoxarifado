package com.almoxarifado.service;

import com.almoxarifado.entity.Item;
import com.almoxarifado.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstoqueService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> listarItensAtivos() {
        return itemRepository.findAllByAtivoTrue();
    }

    public boolean cadastrarItem(Item item) {
        Optional<Item> existente = itemRepository.findByDescricao(item.getDescricao());

        if (existente.isPresent()) {
            if (item.getId() == null || !existente.get().getId().equals(item.getId())) {
                return false;
            }
        }

        itemRepository.save(item);
        return true;
    }

    public void aumentarQuantidadeEmEstoque(Item item, int quantidade) {
        item.setQuantidade(item.getQuantidade() + quantidade);
        itemRepository.save(item);
    }

    public void diminuirQuantidadeEmEstoque(Item item, int quantidade) {
        item.setQuantidade(item.getQuantidade() - quantidade);
        itemRepository.save(item);
    }
    
    public Optional<Item> buscarPorId(Long id) { return itemRepository.findById(id); }

    public void inativarItem(Long id) {
        Item item = this.buscarPorId(id).get();
        item.setAtivo(false);
        itemRepository.save(item);
    }

}
