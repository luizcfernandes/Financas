package br.com.softwarelc.minhasfinancas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.softwarelc.minhasfinancas.model.entity.Lancamento;
import br.com.softwarelc.minhasfinancas.model.enumm.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento,Long>{
    @Query(value="SELECT SUM(l.valor) FROM Lancamento l JOIN l.usuario u WHERE " +
                    " u.id = :idUsuario AND  l.tipo = :tipo GROUP BY u")
    BigDecimal obterSaldoPorTipoLancamentoEUsuario(@Param("idUsuario") Long idUsuario,@Param("tipo") TipoLancamento tipo);
}
