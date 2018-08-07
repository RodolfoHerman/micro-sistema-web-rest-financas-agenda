package br.com.rodolfo.api.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.com.rodolfo.api.enums.PerfilEnum;
import br.com.rodolfo.api.models.Usuario;

/**
 * Converte o Usuario para JWT User do Spring Security
 */
public class JwtUserFactory {

    private JwtUserFactory() {}

    /**
     * Gera e converte para JwtUser baseado nos dados do usuário
     * 
     * @param usuario
     * @return JwtUser
     */
    public static JwtUser create(Usuario usuario) {

        return new JwtUser(usuario.getId(), 
                    usuario.getEmail(), 
                    usuario.getSenha(), 
                    mapToGrantedAuthorities(usuario.getPerfil()));
    }

    /**
     * Converte o perfil do usuário para o formato utilizado pelo Spring Security
     * 
     * @param perfilEnum
     * @return List<GrantedAuthority>
     */
    private static List<GrantedAuthority> mapToGrantedAuthorities(PerfilEnum perfilEnum) {

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(perfilEnum.toString()));

        return authorities;
    }

}