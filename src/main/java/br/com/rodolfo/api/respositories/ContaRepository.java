package br.com.rodolfo.api.respositories;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.rodolfo.api.models.Conta;

/**
 * ContaRepository
 */
@Transactional(readOnly = true)
@NamedQueries({
    @NamedQuery(
        name = "ContaRepository.findByUsuarioEmail",
        query = "SELECT cont FROM Conta cont WHERE cont.usuario.email = :usuarioEmail"
    )
})
public interface ContaRepository extends JpaRepository<Conta, Long> {

    Page<Conta> findByUsuarioEmail(@Param("usuarioEmail") String usuarioEmail, Pageable pageable);

    Conta findByDescricaoIgnoreCaseAndDataVencimentoAndValorFatura(String descricao, Date dataVencimento, BigDecimal valorFatura);

    Page<Conta> findAll(Pageable pageable);
}