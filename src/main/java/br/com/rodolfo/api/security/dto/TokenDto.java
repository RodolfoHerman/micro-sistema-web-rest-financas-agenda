package br.com.rodolfo.api.security.dto;

/**
 * TokenDto
 */
public class TokenDto {

    private String token;

    public TokenDto() {}

    public TokenDto(String token) {

        this.token = token;
    }

    /**
     * @return String return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

}