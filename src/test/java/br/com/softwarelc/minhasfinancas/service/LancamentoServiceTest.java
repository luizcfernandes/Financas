package br.com.softwarelc.minhasfinancas.service;


import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.softwarelc.minhasfinancas.exceptions.RegraNegocioException;
import br.com.softwarelc.minhasfinancas.model.entity.Lancamento;
import br.com.softwarelc.minhasfinancas.model.entity.Usuario;
import br.com.softwarelc.minhasfinancas.model.enumm.StatusLancamento;
import br.com.softwarelc.minhasfinancas.model.enumm.TipoLancamento;
import br.com.softwarelc.minhasfinancas.model.repository.LancamentoRepository;
import br.com.softwarelc.minhasfinancas.model.repository.LancamentoRepositoryTest;
import br.com.softwarelc.minhasfinancas.service.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {
    @SpyBean
    LancamentoServiceImpl service;
    
    @MockBean
    LancamentoRepository repository;

    @Test
    public void deveSalvarUmLancamento(){
        //Cenário
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doNothing().when(service).validar(lancamentoASalvar);
        Lancamento lancamentoSalvo =  LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
      
        ///Ação
        Lancamento lancamento = service.salvar(lancamentoASalvar);
       
        //Verificação
        assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);   
    }

    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
        //Cenário
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
    
        //ação
        catchThrowableOfType(()-> service.salvar(lancamentoASalvar), RegraNegocioException.class);

        //Verificação
        Mockito.verify(repository,Mockito.never()).save(lancamentoASalvar);
    }
   
    @Test
    public void deveAtualizarUmLancamento(){
        //Cenário
        Lancamento lancamentoSalvo =  LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.doNothing().when(service).validar(lancamentoSalvo);
        Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
          
        ///Ação
       service.atualizar(lancamentoSalvo);
        //Verificação
        Mockito.verify(repository,Mockito.times(1)).save(lancamentoSalvo);   
    }

    @Test
    public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
        //Cenário
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();    
        //ação
        catchThrowableOfType(()-> service.atualizar(lancamentoASalvar), NullPointerException.class);

        //Verificação
        Mockito.verify(repository,Mockito.never()).save(lancamentoASalvar);
    }

    @Test
    public void deveDeletarUmLancamento(){
        //cenario
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);

        //ação
        catchThrowableOfType(()-> service.deletar(lancamento), NullPointerException.class);

        //Verificação
        Mockito.verify(repository).delete(lancamento);

    }

    @Test
    public void deveLancarErroAoDeletarUmLancamentoQueAindaNaoFoiSalvo(){
         //cenario
         Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        
         //ação
         catchThrowableOfType(()-> service.deletar(lancamento), NullPointerException.class); 
         //Verificação
         Mockito.verify(repository,Mockito.never()).delete(lancamento);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void deveFiltrarLancamento(){
        //cenario
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);
        List<Lancamento> lista = Arrays.asList(lancamento);
        Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
       
       
        //ação
        List<Lancamento> resultado = service.buscar(lancamento);  

        //verificação
        assertThat(resultado)
        .isNotEmpty()
        .hasSize(1)
        .contains(lancamento);
    }
        @Test
        public void deveAtualizarOStatusDeUmLancamento() {
            //cenario]
            Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
            lancamento.setId(1l);
            lancamento.setStatus(StatusLancamento.PENDENTE);

            StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
            Mockito.doReturn(lancamento).when(service).atualizar(lancamento);

            //ação
            service.atualizarStatus(lancamento,novoStatus);

            //verficação
            Mockito.verify(service).atualizar(lancamento);
            
        }


        @Test
        public void deveObterUmLancamentoPorId(){
            //cenário
            Long id = 1l;
            Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
            lancamento.setId(id);
            Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));

            //execucao
            Optional<Lancamento> resultado = service.obterPorId(id);

            //verificação
            assertThat(resultado.isPresent()).isTrue();

        }

              
        @Test
        public void deveRetornarVazioQuandoOLancamentoNaoExite(){
            //cenário
            Long id = 1l;
            Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
            lancamento.setId(id);
            Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

            //execucao
            Optional<Lancamento> resultado = service.obterPorId(id);

            //verificação
            assertThat(resultado.isPresent()).isFalse();
        }

        @Test
        public void deveLancarErrosAoValidar() {
            Lancamento lancamento =  new Lancamento();
           
            Throwable erro = catchThrowable(()-> service.validar(lancamento));
            assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uam Descrição válida");

            lancamento.setDescricao("");
            erro = catchThrowable(()-> service.validar(lancamento));
            assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uam Descrição válida");

            lancamento.setDescricao("Lançamento de despesas");
            erro = catchThrowable(()-> service.validar(lancamento));
            assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido");

            lancamento.setMes(0);
            erro = catchThrowable(()-> service.validar(lancamento));
            assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido");

            lancamento.setMes(13);
            erro = catchThrowable(()-> service.validar(lancamento));
            assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido");
            
            lancamento.setMes(1);
            erro = catchThrowable(()-> service.validar(lancamento));
            assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido");

            lancamento.setAno(202);
            erro = catchThrowable(()-> service.validar(lancamento));
            assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido");

            lancamento.setAno(2020);
            erro = catchThrowable(()-> service.validar(lancamento));
            assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário válido");

            lancamento.setUsuario(new Usuario());
            erro = catchThrowable(()-> service.validar(lancamento));
            assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário válido");

            lancamento.setUsuario(new Usuario());
            lancamento.getUsuario().setId(1l);
            erro = catchThrowable(()-> service.validar(lancamento));
            assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido");

            lancamento.setValor(BigDecimal.ONE);
            erro = catchThrowable(()-> service.validar(lancamento));
            assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Tipo válido");

            lancamento.setValor(BigDecimal.valueOf(1));
            erro = catchThrowable(()-> service.validar(lancamento));
            assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Tipo válido");

           
            lancamento.setTipo(TipoLancamento.DESPESA);
            erro = catchThrowable(()-> service.validar(lancamento));
            assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Status válido");
        }


}

