package br.com.softwarelc.minhasfinancas.service;

import java.util.Optional;

import br.com.softwarelc.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
    
    Usuario autenticar(String email, String senha);
    Usuario salvarUsuario(Usuario usuario);
    void validarEmail(String email);
    Optional <Usuario> obterPorId(Long id);
}
  