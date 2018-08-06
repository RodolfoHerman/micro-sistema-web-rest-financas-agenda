package br.com.rodolfo.api.respositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.rodolfo.api.models.Usuario;

/**
 * UsuarioRepository
 */
@Transactional(readOnly = true)
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);

    Page<Usuario> findAll(Pageable pageable);
    
}