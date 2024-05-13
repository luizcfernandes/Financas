package br.com.softwarelc.minhasfinancas.service;

import br.com.softwarelc.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
    
    Usuario autenticar(String email, String senha);
    Usuario salvarUsuario(Usuario usuario);
    void validarEmail(String email);
}
 