package br.com.softwarelc.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.softwarelc.minhasfinancas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class UsuarioRepositoryTest {
    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveVerificarAExistenciaDeUmEmail(){
        //cenario
        Usuario usuario = Usuario.builder().nome("luiz carlos").email("lz@gmail.com").senha("1234").build();
        entityManager.persist(usuario);

        //acao/execucao
        boolean result = repository.existsByEmail("lz@gmail.com");

        //verificacao
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastraadoComOEmail(){
        //cenário
        
        //acao
        boolean result = repository.existsByEmail("usuario@gmail.com");

        //vericacao
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void devePersistirUmUsuarioNaBaseDeDados() {
        //cenário
        Usuario usuario = criarUsuaario();
        //acao
        entityManager.persist(usuario);

        //vericação
        Assertions.assertThat(usuario.getId()).isNotNull();
    }

    @Test
    public void deveBuscarUmUsuarioPorEmail(){
        //cenário
        Usuario usuario = criarUsuaario();
        //acao
        entityManager.persist(usuario);

        //vericação
        Optional<Usuario> result = repository.findByEmail("usuario@email.com");
        Assertions.assertThat(result.isPresent()).isTrue(); 
    }

    @Test
    public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteBase(){
        
        //vericação
        Optional<Usuario> result = repository.findByEmail("usuario@email.com");
        Assertions.assertThat(result.isPresent()).isFalse(); 
    }

    public static Usuario criarUsuaario(){
        return Usuario 
                .builder()
                .nome("usuario")
                .email("usuario@email.com")
                .senha("senha")
                .build();
    }

    
}
    
