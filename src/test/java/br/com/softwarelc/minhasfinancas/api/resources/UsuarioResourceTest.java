package br.com.softwarelc.minhasfinancas.api.resources;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.softwarelc.minhasfinancas.api.dto.UsuarioDTO;
import br.com.softwarelc.minhasfinancas.exceptions.ErroAutenticacao;
import br.com.softwarelc.minhasfinancas.exceptions.RegraNegocioException;
//import br.com.softwarelc.minhasfinancas.api.dto.UsuarioDTO;
import br.com.softwarelc.minhasfinancas.model.entity.Usuario;
import br.com.softwarelc.minhasfinancas.service.LancamentoService;
import br.com.softwarelc.minhasfinancas.service.UsuarioService;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioResource.class)
@AutoConfigureMockMvc
public class UsuarioResourceTest {
    static final String API = "/api/usuarios";
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    UsuarioService service;
    
    @MockBean
    LancamentoService lancamentoService;

    @Test
    public void deveAutenticarUmUsuario() throws Exception {
        //cenario
        String email = "email@email.com";
        String senha = "123";
       // UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
        Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
        Mockito.when(service.autenticar(email,senha)).thenReturn(usuario);
        String json = new ObjectMapper().writeValueAsString(usuario);

        //execução e 1verificação
        MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
                                                    .post(API.concat("/autenticar"))
                                                    .accept(JSON)
                                                    .contentType(JSON)
                                                    .content(json);

        try {
            mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()))
                ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    
    @Test
    public void deveRetornarBadrequestAoObterErroDeAutenticacao() throws Exception {
        //cenario
        String email = "email@email.com";
        String senha = "123";
       
        Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
        Mockito.when(service.autenticar(email,senha)).thenThrow(ErroAutenticacao.class);
        String json = new ObjectMapper().writeValueAsString(usuario);

        //execução e 1verificação
        MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
                                                    .post(API.concat("/autenticar"))
                                                    .accept(JSON)
                                                    .contentType(JSON)
                                                    .content(json);

        try {
            mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void deveCriarUmNovoUsuario() throws JsonProcessingException {
        String email = "senha@senha.com";
        String senha = "123";
       
         Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
         try {
            Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         String json = new ObjectMapper().writeValueAsString(usuario);
 
         //execução e 1verificação
         MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
                                                     .post(API)
                                                     .accept(JSON)
                                                     .contentType(JSON)
                                                     .content(json);
 
         try {
             mvc
                 .perform(request)
                 .andExpect(MockMvcResultMatchers.status().isCreated())
                 .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId().toString()))
                 .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                 .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()))
                 ;
         } catch (Exception e) {
             e.printStackTrace();
         }

    }

    @Test
    public void deveRetonarUmBadrequestAoCriarUmUsuarioInvalido() throws JsonProcessingException {
        String email = "senha@senha.com";
        String senha = "123";
       
         Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
         Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);
        
         String json = new ObjectMapper().writeValueAsString(usuario);
 
         //execução e 1verificação
         MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
                                                     .post(API)
                                                     .accept(JSON)
                                                     .contentType(JSON)
                                                     .content(json);
 
         try {
             mvc
                 .perform(request)
                 .andExpect(MockMvcResultMatchers.status().isBadRequest());
               
         } catch (Exception e) {
             e.printStackTrace();
         }

    }
}
