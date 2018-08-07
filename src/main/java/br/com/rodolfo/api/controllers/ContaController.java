package br.com.rodolfo.api.controllers;

import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import br.com.rodolfo.api.dtos.ContaDto;
import br.com.rodolfo.api.enums.TipoEnum;
import br.com.rodolfo.api.models.Conta;
import br.com.rodolfo.api.models.Usuario;
import br.com.rodolfo.api.response.Response;
import br.com.rodolfo.api.services.ContaService;

/**
 * ContaController
 */
@RestController
@RequestMapping("/contas")
@CrossOrigin(origins = "*")
public class ContaController {

    private static final Logger log = LoggerFactory.getLogger(ContaController.class);

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private ContaService contaService;

    @Value("${paginacao.qtd_por_pagina}")
    private int qtdPorPagina;

    /**
     * Método responsável por listar as contas e realizar paginação
     * 
     * @param pag
     * @param ord
     * @param dir
     * @return ResponseEntity<Response<Page<ContaDto>>>
     */
    @GetMapping
    public ResponseEntity<Response<Page<ContaDto>>> listar(
        @RequestParam(value = "pag", defaultValue = "0") int pag,
        @RequestParam(value = "ord", defaultValue = "id") String ord,
        @RequestParam(value = "dir", defaultValue = "ASC") String dir
    ) {
        
        log.info("Listando todas as contas.");

        Response<Page<ContaDto>> response = new Response<Page<ContaDto>>();

        PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
        Page<Conta> contas = this.contaService.buscar(pageRequest);
        Page<ContaDto> contasDto = contas.map(conta -> this.converterContaDto(conta));

        response.setData(contasDto);

        return ResponseEntity.ok(response);
    }

    /**
     * Método responsável por adiiconar conta na base de dados
     * 
     * @param contaDto
     * @param result
     * @return ResponseEntity<Response<ContaDto>>
     * @throws ParseException
     */
    @PostMapping
    public ResponseEntity<Response<ContaDto>> adicionar(@Valid @RequestBody ContaDto contaDto, BindingResult result) throws ParseException {

        log.info("Adicionando conta : {}", contaDto);

        Response<ContaDto> response = new Response<ContaDto>();

        Conta conta = this.converterDtoParaConta(contaDto, result);

        this.validarDadosExistentes(contaDto, result);

        if(result.hasErrors()) {

            log.error("Erro validando dados de cadastro da conta : {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }

        Conta c = this.contaService.persistir(conta);
        response.setData(converterContaDto(conta));

        URI uri = URI.create("/contas/" + c.getId());

        return ResponseEntity.created(uri).body(response);
    }
    
    /**
     * Método responsável por remover uma conta da base de dados
     * 
     * @param id
     * @param result
     * @return ResponseEntity<Response<String>>
     */
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
        
        log.info("Removendo conta com ID : {}", id);

        Response<String> response = new Response<String>();

        Optional<Conta> conta = this.contaService.buscarPorId(id);

        if(!conta.isPresent()) {

            log.error("Erro ao remover conta de ID : {} . Não encontrada", id);
            response.getErrors().add("Erro ao remover conta. Registro não encontrado para o ID : " + id);

            return ResponseEntity.badRequest().body(response);
        }

        this.contaService.remover(id);
        response.setData("Removido com sucesso");

        return ResponseEntity.ok(response);
    }

    /**
     * Método responsável por atualizar os dados da conta
     * 
     * @param id
     * @param contaDto
     * @param result
     * @return ResponseEntity<Response<ContaDto>>
     * @throws ParseException
     */
    @PutMapping(value = "{id}")
    public ResponseEntity<Response<ContaDto>> atualizar(
        @PathVariable("id") Long id,
        @Valid @RequestBody ContaDto contaDto,
        BindingResult result
    ) throws ParseException {
        
        log.info("Atualizando conta : {}", contaDto);

        Response<ContaDto> response = new Response<ContaDto>();

        contaDto.setId(Optional.of(id));

        Conta conta = this.converterDtoParaConta(contaDto, result);

        if(result.hasErrors()) {

            log.error("Erro validadndo dados de atualização da conta : {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(response);
        }

        this.contaService.persistir(conta);
        response.setData(this.converterContaDto(conta));

        return ResponseEntity.ok(response);
    }


    /**
     * Método responsável por converter os dados Conta para DTO
     * 
     * @param conta
     * @return ContaDto
     */
    private ContaDto converterContaDto(Conta conta) {

        ContaDto contaDto = new ContaDto();

        contaDto.setDescricao(conta.getDescricao());
        contaDto.setValorFatura(conta.getValorFatura().toString());
        contaDto.setDataVencimento(this.formatter.format(conta.getDataVencimento()));
        contaDto.setTipo(conta.getTipo().toString());
        conta.getDataPagamentoOpt().ifPresent(data -> contaDto.setDataPagamento(Optional.of(this.formatter.format(data))));
        conta.getValorPagamentoOpt().ifPresent(valor -> contaDto.setValorPagamento(Optional.of(valor.toString())));

        return contaDto;
	}


	/**
     * Método responsável por converter os dados DTO para a classe Conta
     * 
     * @param contaDto
     * @param result
     * @return Conta
     * @throws ParseException
     */
	private Conta converterDtoParaConta(ContaDto contaDto, BindingResult result) throws ParseException  {
        
        Conta conta = new Conta();
        
        if(contaDto.getId().isPresent()) {

            Optional<Conta> cont = this.contaService.buscarPorId(contaDto.getId().get());

            if(cont.isPresent()) {

                conta = cont.get();

            } else {

                result.addError(new ObjectError("conta", "Conta não encontrada"));
            }

        } else {

            conta.setUsuario(new Usuario());
            conta.getUsuario().setId(contaDto.getUsuarioId());
        }

        final Conta c = conta;

        c.setDescricao(contaDto.getDescricao());
        c.setValorFatura(new BigDecimal(contaDto.getValorFatura()));
        c.setDataVencimento(this.formatter.parse(contaDto.getDataVencimento()));

        c.setValorPagamento(null);
        contaDto.getValorPagamento().ifPresent(valorPagamento -> c.setValorPagamento(new BigDecimal(valorPagamento)));
        
        c.setDataPagamento(null);
        contaDto.getDataPagamento().ifPresent(dataPagamento -> {
			try {
				c.setDataPagamento(this.formatter.parse(dataPagamento));
			} catch (ParseException e) {
                
                result.addError(new ObjectError("date", "Data de pagamento inválida."));
			}
		});
        
        if(EnumUtils.isValidEnum(TipoEnum.class, contaDto.getTipo())) {

            c.setTipo(TipoEnum.valueOf(contaDto.getTipo()));

            if(contaDto.getTipo().equals("QUITADA") && (!contaDto.getDataPagamento().isPresent() || !contaDto.getValorPagamento().isPresent())) {

                result.addError(new ObjectError("conta", "Ao selecionar a opção 'QUITADA' é necessário informar Data e Valor de pagamento."));
            }

        } else {

            result.addError(new ObjectError("tipo", "Status inválido"));
        }

        return c;
    }
    
    /**
     * Método que verifica se a conta com descrição, data de vencimento e valor da fatura não existe na base de dados
     * 
     * @param contaDto
     * @param result
     * @throws ParseException
     */
    private void validarDadosExistentes(ContaDto contaDto, BindingResult result) throws ParseException {

        this.contaService.buscarPorDescricaoDataVencimentoValorFatura(
            contaDto.getDescricao(), 
            this.formatter.parse(contaDto.getDataVencimento()), 
            new BigDecimal(contaDto.getValorFatura())
        ).ifPresent(conta -> result.addError(new ObjectError("conta", "Conta já cadastrada no sistema.")));

	}
    
}