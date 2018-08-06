package br.com.rodolfo.api.services;

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