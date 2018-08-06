package br.com.rodolfo.api.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.NumberFormat;

import br.com.rodolfo.api.enums.TipoEnum;

/**
 * Conta
 */
@Entity
@Table(name = "conta")
public class Conta implements Serializable {

    private static final long serialVersionUID = 7711471378452631025L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "valor_fatura", nullable = false)
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal valorFatura;

    @Column(name = "valor_pagamento")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal valorPagamento;

    @Column(name = "data_vencimento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataVencimento;

    @Column(name = "data_pagamento")
    @Temporal(TemporalType.DATE)
    private Date dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoEnum tipo;

    @Column(name = "data_criacao", nullable = false)
    private Date dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    private Date dataAtualizacao;

    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;
    
    public Conta() {}

    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return String return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return BigDecimal return the valorFatura
     */
    public BigDecimal getValorFatura() {
        return valorFatura;
    }

    /**
     * @param valorFatura the valorFatura to set
     */
    public void setValorFatura(BigDecimal valorFatura) {
        this.valorFatura = valorFatura;
    }

    @Transient
    public Optional<BigDecimal> getValorPagamentoOpt() {

        return Optional.ofNullable(valorPagamento);
    }

    /**
     * @return BigDecimal return the valorPagamento
     */
    public BigDecimal getValorPagamento() {
        return valorPagamento;
    }

    /**
     * @param valorPagamento the valorPagamento to set
     */
    public void setValorPagamento(BigDecimal valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    /**
     * @return Date return the dataVencimento
     */
    public Date getDataVencimento() {
        return dataVencimento;
    }

    /**
     * @param dataVencimento the dataVencimento to set
     */
    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    @Transient
    public Optional<Date> getDataPagamentoOpt() {
        return Optional.ofNullable(dataPagamento);
    }

    /**
     * @return Date return the dataPagamento
     */
    public Date getDataPagamento() {
        return dataPagamento;
    }

    /**
     * @param dataPagamento the dataPagamento to set
     */
    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    /**
     * @return TipoEnum return the tipo
     */
    public TipoEnum getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(TipoEnum tipo) {
        this.tipo = tipo;
    }

    /**
     * @return Date return the dataCriacao
     */
    public Date getDataCriacao() {
        return dataCriacao;
    }

    /**
     * @param dataCriacao the dataCriacao to set
     */
    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    /**
     * @return Date return the dataAtualizacao
     */
    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    /**
     * @param dataAtualizacao the dataAtualizacao to set
     */
    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    /**
     * @return Usuario return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @PrePersist
    public void prePersist() {

        final Date atual = new Date();

        this.dataCriacao = atual;
        this.dataAtualizacao = atual;
    }

    @PreUpdate
    public void preUpdate() {
        
        this.dataAtualizacao = new Date();
    }

    @Override
    public String toString() {
        return "Conta : [id = " + id + ", descricao = " + descricao + ", valor fatura = R$" + valorFatura + ", valor pagamento = R$" + valorPagamento + "]";
    }

}