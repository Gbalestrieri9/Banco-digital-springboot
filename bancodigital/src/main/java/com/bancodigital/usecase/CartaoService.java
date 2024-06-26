package com.bancodigital.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancodigital.dao.impl.JdbcTemplateDaoImpl;
import com.bancodigital.dto.PagamentoDTO;
import com.bancodigital.model.CartaoCredito;
import com.bancodigital.model.Cliente;

@Service
public class CartaoService {

	@Autowired
	public ClienteService clienteService;

	@Autowired
	public JdbcTemplateDaoImpl jdbcTemplateDaoImpl;

	public Double criarCartao(String cpf, String categoriaConta) {

		double limiteCartao = 0;
		switch (categoriaConta.toLowerCase()) {
		case "comum":
			limiteCartao = 1000.0;
			break;
		case "super":
			limiteCartao = 5000.0;
			break;
		case "premium":
			limiteCartao = 10000.0;
			break;
		}
		jdbcTemplateDaoImpl.criarCartaoDeCredito(cpf, limiteCartao);
		return limiteCartao;
	}

	public String efetuarPagamento(String cpfCliente, PagamentoDTO pagamento) {
		Cliente cliente = jdbcTemplateDaoImpl.buscarClientePorCpf(cpfCliente);

		if (cliente == null) {
			return "Cliente não encontrado.";
		}

		double valorPagamento = pagamento.getValor();
		String tipoPagamento = pagamento.getTipoPagamento().toLowerCase();

		if (tipoPagamento.equalsIgnoreCase("debito")) {
			if (cliente.getSaldo() < valorPagamento) {
				return "Saldo insuficiente para o pagamento.";
			}
			double novoSaldo = cliente.getSaldo() - valorPagamento;
			
			jdbcTemplateDaoImpl.atualizarSaldoCliente(cpfCliente, novoSaldo);
			
			return "Pagamento efetuado com sucesso no débito.";
		} else if (tipoPagamento.equalsIgnoreCase("credito")) {
			CartaoCredito dadosCartaoDeCredito = recuperarCartaoCredito(cpfCliente);
			
			double novoLimiteCartao = dadosCartaoDeCredito.getLimiteCredito() - valorPagamento;
			double novaFatura = dadosCartaoDeCredito.getValorFatura() + valorPagamento;
			
			jdbcTemplateDaoImpl.atualizarLimiteCartao(cpfCliente, novoLimiteCartao);
		    jdbcTemplateDaoImpl.atualizarValorFatura(cpfCliente, novaFatura);
		    
		    if ((novoLimiteCartao / dadosCartaoDeCredito.getLimiteCredito()) <= 0.8) {
	            aplicarTaxaUtilizacao(cpfCliente);
	        }
		    
			return "Pagamento efetuado com sucesso no crédito.";
		} else {
			return "Tipo de pagamento inválido.";
		}
	}
	
	public CartaoCredito recuperarCartaoCredito(String cpfCliente) {
		return jdbcTemplateDaoImpl.consultarCartaoDeCredito(cpfCliente);  
    }
	
	public void aplicarTaxaUtilizacao(String cpfCliente) {
	    CartaoCredito cartaoCredito = recuperarCartaoCredito(cpfCliente);
	    double limiteCredito = cartaoCredito.getLimiteCredito();
	    double valorFatura = cartaoCredito.getValorFatura();
	    
	    double taxaUtilizacao = valorFatura / limiteCredito;
	    
	    if (taxaUtilizacao >= 0.8) {
	        double taxa = valorFatura * 0.05;
	        double novaFatura = valorFatura + taxa;
	        
	        jdbcTemplateDaoImpl.atualizarValorFatura(cpfCliente, novaFatura);
	    }
	}
	
	public Double gerarApoliceSeguroViagem(String categoriaConta) {
	    if (categoriaConta.equalsIgnoreCase("premium")) {
	        // Seguro viagem gratuito para categoria premium
	        return 0.0;
	    } else {
	        // Para outras categorias, o seguro viagem custa 50.00
	        return 50.0;
	    }
	}

	public String gerarApoliceSeguroFraude() {
	    // O seguro de fraude tem um valor base de 5000
	    return "Cobertura automática para todos os cartões, com um valor de apólice base de 5000.";
	}

	public void salvarApoliceViagem(String cpfCliente, Double valorApolice) {
	    Long cartaoCreditoId = jdbcTemplateDaoImpl.buscarCartaoCreditoIdPorCpfCliente(cpfCliente);
	    jdbcTemplateDaoImpl.salvarApoliceViagem(cpfCliente,cartaoCreditoId, valorApolice);
	}

	public void salvarApoliceFraude(String cpfCliente, String detalhesApolice) {
	    jdbcTemplateDaoImpl.salvarApoliceFraude(cpfCliente, detalhesApolice);
	}
}
