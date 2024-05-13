package br.com.softwarelc.minhasfinancas.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import br.com.softwarelc.minhasfinancas.model.enumm.StatusLancamento;
import br.com.softwarelc.minhasfinancas.model.enumm.TipoLancamento;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="lancamento",schema="financas")
public class Lancamento {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="descricao")
    private String descricao;

    @Column(name="mes")
    private Integer mes;

    @Column(name="ano")
    private Integer ano;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

    @Column(name="valor")
    private BigDecimal valor;

    @Column(name="data_cadastro")
    @Convert(converter=Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate dataCadastro;

    @Column(name="tipo")
    @Enumerated(value=EnumType.STRING)
    private TipoLancamento tipo;

    @Column(name="status")
    @Enumerated(value=EnumType.STRING)
    private StatusLancamento status;

}
