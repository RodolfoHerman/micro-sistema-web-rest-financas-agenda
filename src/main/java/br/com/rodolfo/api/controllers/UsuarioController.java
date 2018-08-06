package br.com.rodolfo.api.controllers;

import java.net.URI;
import java.text.ParseException;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.rodolfo.api.dtos.UsuarioDto;
import br.com.rodolfo.api.enums.PerfilEnum;
import br.com.rodolfo.api.models.Usuario;
import br.com.rodolfo.api.response.Response;
import br.com.rodolfo.api.services.UsuarioService;
import br.com.rodolfo.api.utils.PasswordUtils;

/**
 * UsuarioController
 */
@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    @Value("${paginacao.qtd_por_pagina}")
    private int qtdPorPagina;

    public UsuarioController() {}

    /**
     * Método responsável por listar os usuários e realizar paginação
     * 
     * @param pag
     * @param ord
     * @param dir
     * @return ResponseEntity<Response<Page<UsuarioDto>>>
     */
    @GetMapping
    public ResponseEntity<Response<Page<UsuarioDto>>> listar(
        @RequestParam(value = "pag", defaultValue = "0") int pag,
        @RequestParam(value = "ord", defaultValue = "id") String ord,
        @RequestParam(value = "dir", defaultValue = "DESC") String dir
    ) {
        
        log.info("Listando toso os usuários.");

        Response<Page<UsuarioDto>> response = new Response<Page<UsuarioDto>>();

        PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
        Page<Usuario> usuarios = this.usuarioService.buscarTodos(pageRequest);
        Page<UsuarioDto> usuariosDto = usuarios.map(usuario -> this.converterUsuarioDto(usuario));

        response.setData(usuariosDto);

        return ResponseEntity.ok(response);
    }

    /**
     * Método responsável por adicionar usuário na base de dados
     * 
     * @param usuarioDto
     * @param result
     * @return ResponseEntity<Response<UsuarioDto>>
     * @throws ParseException
     */
    @PostMapping
    public ResponseEntity<Response<UsuarioDto>> adicionar(@Valid @RequestBody UsuarioDto usuarioDto, BindingResult result) throws ParseException {
        
        log.info("Adicionando usuário : {}", usuarioDto);

        Response<UsuarioDto> response = new Response<UsuarioDto>();
        Usuario usuario = this.converterDtoParaUsuario(usuarioDto, result);

        this.validarDadosExistentes(usuarioDto, result);

        if(result.hasErrors()) {

            log.error("Erro validando dados de cadastro do usuário : {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }

        Usuario u = this.usuarioService.persistir(usuario);
        response.setData(this.converterUsuarioDto(usuario));

        URI uri = URI.create("/usuarios/" + u.getId());

        return ResponseEntity.created(uri).body(response);
    }

    /**
     * Método responsável por remover um usuário na base de dados
     * 
     * @param id
     * @param result
     * @return ResponseEntity<Response<String>>
     */
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id, BindingResult result) {

        log.info("Removendo usuário com ID : {}", id);

        Response<String> response = new Response<String>();

        Optional<Usuario> usuario = this.usuarioService.buscarPorId(id);

        if(!usuario.isPresent()) {

            log.error("Erro ao remover usuário de ID : {} . Não encontrado", id);
            response.getErrors().add("Erro ao remover usuário. Registro não encontrado para o ID : " + id);

            return ResponseEntity.badRequest().body(response);
        }

        this.usuarioService.remover(id);
        response.setData("Removido com sucesso");

        return ResponseEntity.ok(response);
    }

    /**
     * Método responsável por atualizar os dados do usuário
     * 
     * @param id
     * @param usuarioDto
     * @param result
     * @return ResponseEntity<Response<UsuarioDto>>
     */
    @PutMapping(value = "{id}")
    public ResponseEntity<Response<UsuarioDto>> atualizar(
        @PathVariable("id") Long id,
        @Valid @RequestBody UsuarioDto usuarioDto, 
        BindingResult result) {

        log.info("Adicionando usuário : {}", usuarioDto);

        Response<UsuarioDto> response = new Response<UsuarioDto>();

        usuarioDto.setId(Optional.of(id));

        Usuario usuario = this.converterDtoParaUsuario(usuarioDto, result);

        if(result.hasErrors()) {

            log.error("Erro validando dados de cadastro de usuário : {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }

        this.usuarioService.persistir(usuario);
        response.setData(this.converterUsuarioDto(usuario));

        return ResponseEntity.ok(response);
    }
    
    /**
     * Método responsável por converter so dados Usuario para DTO
     * 
     * @param usuario
     * @return UsuarioDto
     */
    private UsuarioDto converterUsuarioDto(Usuario usuario) {
        
        UsuarioDto usuarioDto = new UsuarioDto();

        usuarioDto.setId(Optional.of(usuario.getId()));
        usuarioDto.setNome(usuario.getNome());
        usuarioDto.setEmail(usuario.getEmail());
        usuarioDto.setPerfil(usuario.getPerfil().toString());
        
        return usuarioDto;
	}

	/**
     * Método responsável por converter os dados DTO para a classe Usuario
     * 
     * @param usuarioDto
     * @return Usuario
     */
    private Usuario converterDtoParaUsuario(UsuarioDto usuarioDto, BindingResult result) {
        
        Usuario usuario = new Usuario();

        if(usuarioDto.getId().isPresent()) {

            Optional<Usuario> usu = this.usuarioService.buscarPorId(usuarioDto.getId().get());

            if(usu.isPresent()) {

                usuario = usu.get();

            } else {

                result.addError(new ObjectError("usuario", "Usuário não encontrado"));
            }

        }

        usuario.setNome(usuarioDto.getNome());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setSenha(PasswordUtils.gerarBCrypt(usuarioDto.getSenha()));

        if(EnumUtils.isValidEnum(PerfilEnum.class, usuarioDto.getPerfil())) {

            usuario.setPerfil(PerfilEnum.valueOf(usuarioDto.getPerfil()));

        } else {

            result.addError(new ObjectError("perfil", "Perfil inválido"));
        }
        
        return usuario;
	}

	/**
     * Método que verifica se o usuário com o email informado não existe na base de dados
     * 
     * @param usuarioDto
     * @param result
     */
    private void validarDadosExistentes(UsuarioDto usuarioDto, BindingResult result) {

        this.usuarioService.buscarPorEmail(usuarioDto.getEmail())
                .ifPresent(usuario -> result.addError(new ObjectError("usuario", "Email já cadastrado no sistema")));

    }
    
}