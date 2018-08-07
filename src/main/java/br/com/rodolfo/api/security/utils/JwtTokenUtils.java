package br.com.rodolfo.api.security.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Responsável por realizar todo o controle do Token Jwt
 */
@Component
public class JwtTokenUtils {

    private String CLAIM_KEY_USERNAME = "sub";
    private String CLAIM_KEY_ROLE = "role";
    private String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Extrair o username (email) contido no Token JWT
     * 
     * @param token
     * @return String
     */
    public String extrairUsername(String token) {

        String username;

        try {
            
            Claims claims = this.extrairClaims(token);
            username = claims.getSubject();

        } catch (Exception e) {
            
            username = null;
        }

        return username;
    }

    /**
     * Extrair a data de expiração contido no Token JWT
     * Verifica se o Token está expirado ou não
     * 
     * @param token
     * @return Date
     */
    public Date extrairDataExpiracao(String token) {

        Date expiracao;

        try {
            
            Claims claims = this.extrairClaims(token);
            expiracao = claims.getExpiration();

        } catch (Exception e) {
            
            expiracao = null;
        }

        return expiracao;
    }

    /**
     * Retorna o Token JWT com nova data de expiração
     * 
     * @param token
     * @return String
     */
    public String refreshToken(String token) {

        String refreshToken;

        try {
            
            Claims claims = this.extrairClaims(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshToken = this.gerarToken(claims);

        } catch (Exception e) {
            
            refreshToken = null;
        }

        return refreshToken;
    }


    /**
     * Verifica se o Token JWT é válido
     * 
     * @param token
     * @return boolean
     */
    public boolean tokenValido(String token) {

        return !this.tokenExpirado(token);
    }

    /**
     * Retorna um token JWT com base no username e role (perfil)
     * 
     * @param userDetails
     * @return String
     */
    public String obterToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<String, Object>();

        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        userDetails.getAuthorities().forEach(authority -> claims.put(CLAIM_KEY_ROLE, authority.getAuthority()));
        claims.put(CLAIM_KEY_CREATED, new Date());

        return this.gerarToken(claims);
    }

    /**
     * Verifca se um Token JWT está expirado
     * 
     * @param token
     * @return boolean
     */
    private boolean tokenExpirado(String token) {

        Date dataExpiracao = this.extrairDataExpiracao(token);

        if(dataExpiracao == null) {

            return false;
        }

        return dataExpiracao.before(new Date());
    }

    /**
     * Retorna a nova data de expiração com base na data atual
     * 
     * @return Date
     */
    public Date gerarDataExpiracao() {

        return new Date(System.currentTimeMillis() + this.expiration * 1000);
    }

    /**
     * Realiza o parse do token para extrair as informações (claims)
     * 
     * @param token
     * @return Claims
     */
    private Claims extrairClaims(String token) {

        Claims claims;

        try {
            
            claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();

        } catch (Exception e) {
            
            claims = null;
        }

        return claims;
    }

    /**
     * Gerar um novo Token JWT com os dados (claims) fornecidos
     * 
     * @param claims
     * @return String
     */
    private String gerarToken(Map<String, Object> claims) {

        return Jwts.builder()
                   .setClaims(claims)
                   .setExpiration(this.gerarDataExpiracao())
                   .signWith(SignatureAlgorithm.HS512, this.secret)
                   .compact();
    }
    
}