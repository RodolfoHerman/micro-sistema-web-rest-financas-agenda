package br.com.rodolfo.api.security.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * JwtAuthenticationDto
 */
public class JwtAuthenticationDto {

    @NotEmpty(message = "Email não pode ser vazio.")
    @Email(message = "Email inválido.")
    private String email;

    @NotEmpty(message = "Senha não pode ser vazia.")
    private String senha;

    public JwtAuthenticationDto() {}

    /**
     * @return String return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return String return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "JwtAuthenticationDto [email : " + email + "]";
    }
}