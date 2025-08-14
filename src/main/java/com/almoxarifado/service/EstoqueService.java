package com.almoxarifado.service;

import com.almoxarifado.entity.Item;
import com.almoxarifado.entity.TipoItem;
import com.almoxarifado.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstoqueService {

    @Autowired
    private ItemRepository itemRepository;

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
    
    public Optional<Item> buscarPorId(Long id) { return itemRepository.findById(id); }

    public List<Item> listarItens() {
        return itemRepository.findAll();
    }

    public void deletarItem(Long id) {
        itemRepository.deleteById(id);
    }
}
