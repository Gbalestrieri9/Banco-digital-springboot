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
		    
			return "Pagamento efetuado com sucesso no crédito.";
		} else {
			return "Tipo de pagamento inválido.";
		}
	}
	
	public CartaoCredito recuperarCartaoCredito(String cpfCliente) {
		return jdbcTemplateDaoImpl.consultarCartaoDeCredito(cpfCliente);  
    }

}
