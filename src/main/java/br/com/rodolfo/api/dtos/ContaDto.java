package br.com.rodolfo.api.dtos;

import java.util.Optional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

/**
 * ContaDto
 */
public class ContaDto {

    private Optional<Long> id = Optional.empty();
    
    @NotEmpty(message = "Descrição não pode ser vazia.")
    @Length(min = 3, max = 200, message = "Descricão deve conter entre 3 e 200 caracteres")
    private String descricao;

    @NotEmpty(message = "Valor da fatura deve ser informado.")
    private String valorFatura;

    @NotEmpty(message = "Data de vencimento deve ser informada.")
    @Pattern(regexp = "((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])", message = "Formato da data inválido")
    private String dataVencimento;

    @NotEmpty(message = "O status da fatura deve ser informado")
    private String tipo;

    private Long usuarioId;

    private Optional<String> valorPagamento = Optional.empty();

    private Optional<String> dataPagamento = Optional.empty();

    public ContaDto() {}


    /**
     * @return Optional<Long> return the id
     */
    public Optional<Long> getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Optional<Long> id) {
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
     * @return String return the valorFatura
     */
    public String getValorFatura() {
        return valorFatura;
    }

    /**
     * @param valorFatura the valorFatura to set
     */
    public void setValorFatura(String valorFatura) {
        this.valorFatura = valorFatura;
    }

    /**
     * @return String return the dataVencimento
     */
    public String getDataVencimento() {
        return dataVencimento;
    }

    /**
     * @param dataVencimento the dataVencimento to set
     */
    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    /**
     * @return String return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return Long return the usuarioId
     */
    public Long getUsuarioId() {
        return usuarioId;
    }

    /**
     * @param usuarioId the usuarioId to set
     */
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * @return Optional<String> return the valorPagamento
     */
    public Optional<String> getValorPagamento() {
        return valorPagamento;
    }

    /**
     * @param valorPagamento the valorPagamento to set
     */
    public void setValorPagamento(Optional<String> valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    /**
     * @return Optional<String> return the dataPagamento
     */
    public Optional<String> getDataPagamento() {
        return dataPagamento;
    }

    /**
     * @param dataPagamento the dataPagamento to set
     */
    public void setDataPagamento(Optional<String> dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    @Override
    public String toString() {
        return "ContaDto : [descricao = " + descricao + ", valor da fatura = " + valorFatura + ", data de vencimento = " + dataVencimento + ", status = " + tipo + "]";
    }

}