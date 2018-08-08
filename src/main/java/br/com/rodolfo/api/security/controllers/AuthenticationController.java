package br.com.rodolfo.api.security.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rodolfo.api.response.Response;
import br.com.rodolfo.api.security.dto.JwtAuthenticationDto;
import br.com.rodolfo.api.security.dto.TokenDto;
import br.com.rodolfo.api.security.utils.JwtTokenUtils;

/**
 * AuthenticationController
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private static final String TOKEN_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Gera e retorna o TOKEN JWT
     * 
     * @param jwtAuthenticationDto
     * @param resul
     * @return ResponseEntity<Response<TokenDto>>
     */
    @PostMapping
    public ResponseEntity<Response<TokenDto>> gerarTokenJwt(@Valid @RequestBody JwtAuthenticationDto jwtAuthenticationDto, BindingResult result) {
     
        Response<TokenDto> response = new Response<TokenDto>();
        
        if(result.hasErrors()) {

            log.error("Erro na geração do token : {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }
        
        log.info("Gerando token para o email : {} ", jwtAuthenticationDto.getEmail());

        Authentication authentication = this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(jwtAuthenticationDto.getEmail(), jwtAuthenticationDto.getSenha())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails  = this.userDetailsService.loadUserByUsername(jwtAuthenticationDto.getEmail());

        String token = this.jwtTokenUtils.obterToken(userDetails);

        response.setData(new TokenDto(token));

        return ResponseEntity.ok(response);
    }


    /**
     * Realiza o refresh do Token JWT
     * 
     * @param request
     * @return ResponseEntity<Response<TokenDt>>
     */
    @PostMapping(value = "/refresh")
    public ResponseEntity<Response<TokenDto>> refreshToken(HttpServletRequest httpServletRequest) {
        
        log.info("Gerando refresh do Token JWT");

        Response<TokenDto> response = new Response<TokenDto>();

        Optional<String> token = Optional.ofNullable(httpServletRequest.getHeader(TOKEN_HEADER));

        if(token.isPresent() && token.get().startsWith(BEARER_PREFIX)) {

            token = Optional.of(token.get().substring(7));
        }
        
        if(!token.isPresent()) {

            response.getErrors().add("Token não informado.");

        } else if(!this.jwtTokenUtils.tokenValido(token.get())) {

            response.getErrors().add("Token inválido ou expirado.");
        }

        if(!response.getErrors().isEmpty()) {

            log.error("Erro ao dar refresh no token", response.getErrors());

            return ResponseEntity.badRequest().body(response);
        }

        String refreshedToken = this.jwtTokenUtils.refreshToken(token.get());
        response.setData(new TokenDto(refreshedToken));

        return ResponseEntity.ok(response);
    }

}