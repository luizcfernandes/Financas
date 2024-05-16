package br.com.softwarelc.minhasfinancas.api.resources;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.softwarelc.minhasfinancas.api.dto.UsuarioDTO;
import br.com.softwarelc.minhasfinancas.exceptions.ErroAutenticacao;
import br.com.softwarelc.minhasfinancas.exceptions.RegraNegocioException;
import br.com.softwarelc.minhasfinancas.model.entity.Usuario;
import br.com.softwarelc.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioResource {
    
    private final UsuarioService service;
   
    /*public UsuarioResource(UsuarioService service ){
        this.service = service;
    }*/
   
    @PostMapping
    public ResponseEntity<String> salvar(@RequestBody UsuarioDTO dto) throws UnsupportedEncodingException, NoSuchAlgorithmException{

        Usuario usuario = Usuario.builder()
                            .nome(dto.getNome())
                            .email(dto.getEmail())
                            .senha(encriptografarSenha(dto.getSenha()))
                            .build();
        try {
            Usuario usuarioSalvo = service.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo,HttpStatus.CREATED);
        } catch(RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
   
    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) throws UnsupportedEncodingException, NoSuchAlgorithmException{
        try {
            Usuario usuarioAutenticado =  service.autenticar(dto.getEmail(), encriptografarSenha(dto.getSenha()));
            return ResponseEntity.ok(usuarioAutenticado);
        }catch (ErroAutenticacao e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
    //encriptografar a senha
    public String encriptografarSenha(String senha) throws UnsupportedEncodingException,NoSuchAlgorithmException{
        //Obtem instancia de MessageDisget com algoritmo SHA2
        MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
        byte messageDigest[] = algorithm.digest(senha.getBytes("UTF-8"));

        //Converte a senha encriptografada para o formato Hexadecimal
        StringBuilder hexString = new StringBuilder();
        for(byte b: messageDigest){
            hexString.append(String.format("%02X", 0xFF & b));
        }
        //retonar a senha criptografada em formato hexadecimal
        return hexString.toString();
    }
   
   
}

