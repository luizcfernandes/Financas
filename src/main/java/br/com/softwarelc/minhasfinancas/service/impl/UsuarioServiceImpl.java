package br.com.softwarelc.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.softwarelc.minhasfinancas.exceptions.ErroAutenticacao;
import br.com.softwarelc.minhasfinancas.exceptions.RegraNegocioException;
import br.com.softwarelc.minhasfinancas.model.entity.Usuario;
import br.com.softwarelc.minhasfinancas.model.repository.UsuarioRepository;
import br.com.softwarelc.minhasfinancas.service.UsuarioService;
import jakarta.transaction.Transactional;


@Service
public class UsuarioServiceImpl implements UsuarioService{

    
    private UsuarioRepository repository;
    
    @Autowired
    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        // TODO Auto-generated method stub

        Optional<Usuario> usuario = repository.findByEmail(email);

        if(!usuario.isPresent()) {
            throw new ErroAutenticacao("Usuário não encontrado!");
        }
        if(!usuario.get().getSenha().equals(senha)){
            throw new ErroAutenticacao("Senha Inválida");
        }
        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if (existe) {
            throw new RegraNegocioException("Já existe um usuario cadastrado com esse email!");
        }
    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {
        // TODO Auto-generated method stub
        return repository.findById(id);
    }

    
}