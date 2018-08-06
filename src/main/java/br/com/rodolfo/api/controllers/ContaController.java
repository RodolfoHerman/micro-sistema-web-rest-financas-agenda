package br.com.rodolfo.api.controllers;

import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

        return ResponseEntity.ok().body(response);
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
        
        conta.setDataPagamento(null);
        contaDto.getDataPagamento().ifPresent(dataPagamento -> {
			try {
				c.setDataPagamento(this.formatter.parse(dataPagamento));
			} catch (ParseException e) {
                
                result.addError(new ObjectError("date", "Data de pagamento inválida."));
			}
		});
        
        if(EnumUtils.isValidEnum(TipoEnum.class, contaDto.getTipo())) {

            c.setTipo(TipoEnum.valueOf(contaDto.getTipo()));

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