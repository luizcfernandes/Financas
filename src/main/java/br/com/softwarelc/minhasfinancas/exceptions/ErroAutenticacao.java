package br.com.softwarelc.minhasfinancas.exceptions;

public class ErroAutenticacao extends RuntimeException {

    public ErroAutenticacao(String mensage){
        super(mensage);
    }
    
}
