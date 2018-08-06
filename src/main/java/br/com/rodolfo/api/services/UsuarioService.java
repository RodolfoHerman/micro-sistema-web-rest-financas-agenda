package br.com.rodolfo.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import br.com.rodolfo.api.models.Usuario;

/**
 * UsuarioService
 */
public interface UsuarioService {

    /**
     * Persistir usuario na base de dados
     * 
     * @param usuario
     * @return Usuaio
     */
    Usuario persistir(Usuario usuario);

    /**
     * Busca e retorna todos os usuários
     * 
     * @param pageRequest
     * @return Page<Usuario>
     */
    Page<Usuario> buscarTodos(PageRequest pageRequest);

    /**
     * Busca e retorna um usuario dado um id
     * 
     * @param id
     * @return Optional<Usuario>
     */
    Optional<Usuario> buscarPorId(Long id);
    
    /**
     * Busca e retorna um usuario dado um email
     * 
     * @param email
     * @return Optional<Usuario>
     */
    Optional<Usuario> buscarPorEmail(String email);

    /**
     * Remove um usuario na base de dados dado um id
     * 
     * @param id
     * @return void
     */
    void remover(Long id);

}