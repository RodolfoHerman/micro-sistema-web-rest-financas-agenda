package br.com.rodolfo.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.rodolfo.api.models.Usuario;
import br.com.rodolfo.api.respositories.UsuarioRepository;
import br.com.rodolfo.api.services.UsuarioService;

/**
 * UsuarioServiceImpl
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

	@Override
	public Usuario persistir(Usuario usuario) {
        
        log.info("Persistindo o usuario : {}", usuario);
        
        return this.usuarioRepository.save(usuario);
        }

	@Override
	public Optional<Usuario> buscarPorEmail(String email) {
        
        log.info("Buscando usuario pelo EMAIL : {}", email);

        return Optional.ofNullable(this.usuarioRepository.findByEmail(email));
    }

	@Override
	public void remover(Long id) {

        log.info("Removendo o usuario de id : {}", id);
        
        this.usuarioRepository.deleteById(id);
	}

	@Override
	public Optional<Usuario> buscarPorId(Long id) {
                
                log.info("Buscando usuario pelo id : {}", id);
                
                return this.usuarioRepository.findById(id);
	}

	@Override
	public Page<Usuario> buscarTodos(PageRequest pageRequest) {
                
                log.info("Buscando usuários na base de dados");
                
                return this.usuarioRepository.findAll(pageRequest);
	}
    
}