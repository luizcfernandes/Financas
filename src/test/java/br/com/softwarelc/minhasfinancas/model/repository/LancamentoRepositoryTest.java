package br.com.softwarelc.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.softwarelc.minhasfinancas.model.entity.Lancamento;
import br.com.softwarelc.minhasfinancas.model.enumm.StatusLancamento;
import br.com.softwarelc.minhasfinancas.model.enumm.TipoLancamento;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
    @Autowired
    LancamentoRepository repository;

    @Autowired
    TestEntityManager entityManager;
    

    
    @Test
    public void deveSalvarUmLancamento() {
        //cenário
        Lancamento lancamento = criarLancamento();

        //ação
        lancamento = repository.save(lancamento);

        //Verificação
        assertThat(lancamento.getId()).isNotNull();


    }

    @Test
    public void deveDeletarUmLancamento() {
        //cenário
        Lancamento lancamento = criarEPersistirUmLancamento();

        //ação
        entityManager.find(Lancamento.class, lancamento.getId());
        repository.delete(lancamento);
        Lancamento lanamentoInexistente = entityManager.find(Lancamento.class,lancamento.getId());
        
        //Verificação
        assertThat(lanamentoInexistente).isNull();
    }

    @Test
    public void deveAtualizarUmLancamento(){
        //cenário
        Lancamento lancamento = criarEPersistirUmLancamento();

        //ação
        lancamento.setAno(2023);
        lancamento.setDescricao("Teste Atualizar");
        lancamento.setStatus(StatusLancamento.CANCELADO);
        repository.save(lancamento);

        //Verificação
        Lancamento lancamentoAtualizado = entityManager
            .find(Lancamento.class,lancamento.getId());
        assertThat(lancamentoAtualizado.getAno())
                .isEqualTo(2023);
        assertThat(lancamentoAtualizado.getDescricao())
                .isEqualTo("Teste Atualizar");    
        assertThat(lancamentoAtualizado.getStatus())
                .isEqualTo(StatusLancamento.CANCELADO);     
    }

    @Test 
    public void deveBuscarUmLancamentoPorId(){
        //cenario
        Lancamento lancamento = criarEPersistirUmLancamento();

        //ação
        Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());

        //Verficação
        assertThat(lancamentoEncontrado.isPresent()).isTrue();
    }
    public Lancamento criarEPersistirUmLancamento() {
        Lancamento lancamento = criarLancamento();
        lancamento = entityManager.persist(lancamento);
        return lancamento;
    }
    public static  Lancamento criarLancamento(){
        return Lancamento 
                .builder()
                .ano(2024)
                .mes(1)
                .descricao("um lançamento")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .build();               
    }
}
