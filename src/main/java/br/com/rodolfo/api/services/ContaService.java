package br.com.rodolfo.api.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import br.com.rodolfo.api.models.Conta;

/**
 * ContaService
 */
public interface ContaService {

    /**
     * Persistir uma conta na base de dados
     * 
     * @param conta
     * @return Conta
     */
    Conta persistir(Conta conta);

    /**
     * Retorna uma lista paginada de todas as contas
     * 
     * @param pageRequest
     * @return Page<Conta>
     */
    Page<Conta> buscar(PageRequest pageRequest);

    /**
     * Busca e retorna uma conta dado um id
     * 
     * @param id
     * @return Optional<Conta>
     */
    Optional<Conta> buscarPorId(Long id);

    /**
     * Busca e retorna uma conta dado a descricao a data de vencimento e o valor da fatura
     * 
     * @param descricao
     * @param dataVencimento
     * @param valorFatura
     * @return Optional<Conta>
     */
    Optional<Conta> buscarPorDescricaoDataVencimentoValorFatura(String descricao, Date dataVencimento, BigDecimal valorFatura);

    /**
     * Retorna uma lista paginada de contas dado um email
     * 
     * @param email
     * @param pageRequest
     * @return Page<Conta>
     */
    Page<Conta> buscarPorEmail(String email, PageRequest pageRequest);

    /**
     * Remover uma conta na base de dados
     * 
     * @param id
     */
    void remover(Long id);
    
}