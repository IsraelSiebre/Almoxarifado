package com.almoxarifado.service;

import com.almoxarifado.entity.Item;
import com.almoxarifado.repository.ItemRepository;
import com.almoxarifado.specification.ItemSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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


    public List<Item> buscarComFiltro(Integer quantidade,
                                      String unidade,
                                      String marca,
                                      String local,
                                      Long tipoId) {

        Specification<Item> specs = ItemSpecs.ativo();

        if (quantidade != null) {
            specs = specs.and(ItemSpecs.comQuantidade(quantidade));
        }
        if (unidade != null && !unidade.isEmpty()) {
            specs = specs.and(ItemSpecs.comUnidade(unidade));
        }
        if (marca != null && !marca.isEmpty()) {
            specs = specs.and(ItemSpecs.comMarca(marca));
        }
        if (local != null && !local.isEmpty()) {
            specs = specs.and(ItemSpecs.comLocal(local));
        }
        if (tipoId != null) {
            specs = specs.and(ItemSpecs.comTipo(tipoId));
        }

        return itemRepository.findAll(specs, Sort.unsorted());
    }


    public List<Integer> listarQuantidadesUnicas() {
        return itemRepository.findDistinctQuantidades();
    }

    public List<String> listarUnidadesUnicas() {
        return itemRepository.findDistinctUnidades();
    }

    public List<String> listarMarcasUnicas() {
        return itemRepository.findDistinctMarcas();
    }

    public List<String> listarLocaisUnicos() {
        return itemRepository.findDistinctLocais();
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

    public List<Item> buscarItensAbaixoEstoqueMinimo() {
        return itemRepository.findItensAbaixoDoEstoqueMinimo();
    }

}
