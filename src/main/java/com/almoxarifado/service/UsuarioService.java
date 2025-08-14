package com.almoxarifado.service;

import com.almoxarifado.entity.Usuario;
import com.almoxarifado.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean cadastrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return false; // já existe
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
        return true;
    }
    
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public boolean atualizarUsuario(Long id, Usuario novoUsuario) {
        Usuario usuarioAntigo = this.buscarPorId(id).orElseThrow();

        if (!usuarioAntigo.getEmail().equals(novoUsuario.getEmail()) &&
                usuarioRepository.findByEmail(novoUsuario.getEmail()).isPresent()) {
            return false;
        }

        usuarioAntigo.setNome(novoUsuario.getNome());
        usuarioAntigo.setEmail(novoUsuario.getEmail());

        if (!novoUsuario.getSenha().isBlank()) {
            usuarioAntigo.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));
        }

        usuarioRepository.save(usuarioAntigo);
        return true;
    }


}
