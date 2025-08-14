package com.almoxarifado.service;

import com.almoxarifado.entity.TipoItem;
import com.almoxarifado.repository.TipoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoItemService {

    @Autowired
    private TipoItemRepository tipoItemRepository;

    public Optional<TipoItem> buscarPorId(Long id) {
        return tipoItemRepository.findById(id);
    }

    public boolean cadastrarTipoItem(TipoItem tipoItem) {
        Optional<TipoItem> existente = tipoItemRepository.findByNome(tipoItem.getNome());

        if (existente.isPresent()) {
            if (tipoItem.getId() == null || !existente.get().getId().equals(tipoItem.getId())) {
                return false;
            }
        }

        tipoItemRepository.save(tipoItem);
        return true;
    }


    public List<TipoItem> listarTiposItem() {
        return tipoItemRepository.findAll();
    }

    public void deletarTipoItem(Long id) {
        tipoItemRepository.deleteById(id);
    }

}
