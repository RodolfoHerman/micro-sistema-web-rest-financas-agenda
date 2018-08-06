package br.com.rodolfo.api.services.impl;

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

}