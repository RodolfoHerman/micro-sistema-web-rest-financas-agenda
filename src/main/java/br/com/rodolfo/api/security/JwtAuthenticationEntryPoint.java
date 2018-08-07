package br.com.rodolfo.api.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Classe resposável por lançar a excessão padrão de acesso negado
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
        
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, 
            "Acesso negado. Você deve estar autenticado para acessar a URL requerida.");
	}

    
}