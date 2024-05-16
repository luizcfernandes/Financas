package br.com.softwarelc.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.softwarelc.minhasfinancas.exceptions.ErroAutenticacao;
import br.com.softwarelc.minhasfinancas.exceptions.RegraNegocioException;
import br.com.softwarelc.minhasfinancas.model.entity.Usuario;
import br.com.softwarelc.minhasfinancas.model.repository.UsuarioRepository;
import br.com.softwarelc.minhasfinancas.service.impl.UsuarioServiceImpl;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImpl service;
    
    @MockBean
    UsuarioRepository repository;
    /*
    @Before
    public void setUp(){
        service = Mockito.spy(UsuarioServiceImpl.class);
        service = new UsuarioServiceImpl(repository);
    }
    // */

    @Test
    public void deveSalvarUmUsuario(){
        //cenario
        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
        Usuario usuario = criarUsuaario();

        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
        //ação
        Usuario usuarioSalvo = service.salvarUsuario(new Usuario());

        ///verficação
        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
        Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");    
        Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
    }

    @Test(expected = RegraNegocioException.class)
    public void naoDeveSalvarUmUsuarioComEmailJaCadastrado(){
        //cenario
        String email = "email@email.com";
        Usuario usuario = criarUsuaario();
        Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);

        //acao
        service.salvarUsuario(usuario);

        //verificação
        Mockito.verify(repository,Mockito.never()).save(usuario);
    }

    @Test(expected = Test.None.class)
    public void deveValidarEmail(){
        //cenario
        Mockito.when(repository.existsByEmail((Mockito.anyString()))).thenReturn(false);
        
        //acao
        service.validarEmail("email@email.com");
    }

    @Test(expected = RegraNegocioException.class)
    public void deveLancarErroQuandoExistirEmailCadastro(){
        //cenario
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
        //acao
        service.validarEmail("email@email.com");
    }

 
    
    @Test
    public void deveAutenticarUmUsuarioComSucesso(){
       //cenario
       String email = "email@email.com";
       String senha = "senha";
    
       Usuario usuario = criarUsuaario();
       Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

       //acao
       Usuario result = service.autenticar(email, senha);

       //verficacao
       Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado(){
        //cenario
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn((Optional.empty()));
        //acao
        Throwable exception = Assertions.catchThrowable(()-> service.autenticar("email@email.com", "senha"));
        
        //verificação
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado!");

    }
    @Test
    public void deveObterUsuarioPorId(){
        //cenario
        Long id = 1l;
        String email = "email@email.com";
        Usuario usuario = criarUsuaario();
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        //acao
        Optional<Usuario> result = service.obterPorId(id);


        //verficiaçao
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater(){
        //cenario
        String email = "email@email.com";
     
        Usuario usuario = criarUsuaario();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
 
        //acao
        Throwable exception = Assertions.catchThrowable(()-> service.autenticar(email, "123"));

        //verificao
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha Inválida");
    }
    public static Usuario criarUsuaario(){
        return Usuario 
                .builder()
                .id(1l)
                .nome("nome")
                .email("email@email.com")
                .senha("senha")
                .build();
    }
  
}