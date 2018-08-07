package br.com.rodolfo.api.security.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.rodolfo.api.models.Usuario;
import br.com.rodolfo.api.security.JwtUserFactory;
import br.com.rodolfo.api.services.UsuarioService;

/**
 * Obter as informações do usuário
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<Usuario> usuario = this.usuarioService.buscarPorEmail(username);

        if(usuario.isPresent()) {

            return JwtUserFactory.create(usuario.get());
        }

        throw new UsernameNotFoundException("Usuário não encontrado.");
	}

    
}