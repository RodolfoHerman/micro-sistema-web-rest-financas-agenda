package br.com.rodolfo.api.services.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.rodolfo.api.models.Conta;
import br.com.rodolfo.api.respositories.ContaRepository;
import br.com.rodolfo.api.services.ContaService;

/**
 * ContaServiceImpl
 */
@Service
public class ContaServiceImpl implements ContaService {

    private static final Logger log = LoggerFactory.getLogger(ContaServiceImpl.class);
    
    @Autowired
    private ContaRepository contaRepository;
    
    @Override
	public Conta persistir(Conta conta) {
        
        log.info("Persistindo a conta : {}", conta);
        
        return this.contaRepository.save(conta);
	}

	@Override
	public Page<Conta> buscar(PageRequest pageRequest) {
        
        log.info("Buscando contas na base de dados");
        
        return this.contaRepository.findAll(pageRequest);
	}

	@Override
	public Page<Conta> buscarPorEmail(String email, PageRequest pageRequest) {
        
        log.info("Buscando contas para o email : {}", email);
        
        return this.contaRepository.findByUsuarioEmail(email, pageRequest);
	}

	@Override
	public void remover(Long id) {
        
        log.info("Removendo conta de id : {}", id);

        this.contaRepository.deleteById(id);
	}

	@Override
	public Optional<Conta> buscarPorId(Long id) {
                
                log.info("Buscando conta pelo id : {}", id);

                return this.contaRepository.findById(id);
	}

	@Override
	public Optional<Conta> buscarPorDescricaoDataVencimentoValorFatura(String descricao, Date dataVencimento,
			BigDecimal valorFatura) {
                
                log.info("Buscando conta pela descricao, dataVencimento e valorFatura : {}, {}, {}", descricao, dataVencimento, valorFatura);
                
                return Optional.ofNullable(this.contaRepository.findByDescricaoIgnoreCaseAndDataVencimentoAndValorFatura(descricao, dataVencimento, valorFatura));
	}

}