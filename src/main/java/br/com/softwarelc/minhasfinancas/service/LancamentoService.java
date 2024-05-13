package br.com.softwarelc.minhasfinancas.service;

import java.util.List;

import br.com.softwarelc.minhasfinancas.model.entity.Lancamento;
import br.com.softwarelc.minhasfinancas.model.enumm.StatusLancamento;

public interface LancamentoService {

    Lancamento salvar(Lancamento lancamento);
    Lancamento atualizar(Lancamento lancamento);
    void deletar(Lancamento lancamento);
    List<Lancamento> buscar(Lancamento lancamentoFiltro);
    void atualizarStatus(Lancamento lancamento,StatusLancamento status);
    void validar(Lancamento lancamento);
    
}
