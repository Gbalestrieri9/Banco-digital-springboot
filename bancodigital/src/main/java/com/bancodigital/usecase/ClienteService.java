package com.bancodigital.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bancodigital.dao.impl.JdbcTemplateDaoImpl;
import com.bancodigital.model.Cliente;
import com.bancodigital.utils.Constantes;
import com.bancodigital.utils.JwtConfig;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

import java.sql.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ClienteService {

	@Autowired
	private JdbcTemplateDaoImpl jdbcTemplateDaoImpl;

	private static final Pattern CPF_PATTERN = Pattern.compile("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");

	public String criarCliente(String cpf, String nome, String endereco, Date data, String senha, String tipoConta,
			double saldo, String categoriaConta) {
		if (!isValidTipoConta(tipoConta)) {
			return Constantes.MSG_CRIACAO_CLIENTE_TIPO_CONTA_ERRO;
		} else if (!isValidCategoriaConta(categoriaConta)) {
			return Constantes.MSG_CRIACAO_CLIENTE_CATEGORIA_CONTA_ERRO;
		} else if (!isValidCPF(cpf)) {
			return Constantes.MSG_CADASTRO_CPF_ERRO;
		} else {
			jdbcTemplateDaoImpl.criarCliente(cpf, nome, endereco, data, senha, tipoConta, saldo, categoriaConta);
			return Constantes.MSG_CRIACAO_CLIENTE_SUCESSO;
		}
	}

	public List<Cliente> listarClientes() {
		return jdbcTemplateDaoImpl.listarClientes();
	}

	public String transferirSaldo(String cpfOrigem, String cpfDestino, double valor) {
		Cliente clienteOrigem = jdbcTemplateDaoImpl.buscarClientePorCpf(cpfOrigem);
		Cliente clienteDestino = jdbcTemplateDaoImpl.buscarClientePorCpf(cpfDestino);
		String mensagem = "";

		if (clienteOrigem != null && clienteDestino != null && clienteOrigem.getSaldo() >= valor) {
			double novoSaldoOrigem = clienteOrigem.getSaldo() - valor;
			double novoSaldoDestino = clienteDestino.getSaldo() + valor;

			jdbcTemplateDaoImpl.atualizarSaldoCliente(cpfOrigem, novoSaldoOrigem);
			jdbcTemplateDaoImpl.atualizarSaldoCliente(cpfDestino, novoSaldoDestino);

			mensagem = Constantes.MSG_TRANSFERENCIA_SUCESSO;
		} else if (clienteOrigem.getSaldo() < valor) {
			mensagem = Constantes.MSG_TRANSFERENCIA_SALDO_INSUFICIENTE;
		} else {
			mensagem = Constantes.MSG_TRANSFERENCIA_ERRO;
		}
		return mensagem;
	}

	public String login(String cpf, String senha) {
		Cliente cliente = jdbcTemplateDaoImpl.buscarClientePorCpf(cpf);
		if (cliente != null && cliente.getSenha().equals(senha)) {

			Key chaveSecreta = JwtConfig.getChaveSecreta();

			String token = Jwts.builder().claim("cpf", cliente.getCpf()).claim("nome", cliente.getNome())
					.claim("endereco", cliente.getEndereco()).claim("data", cliente.getData())
					.claim("tipoConta", cliente.getTipoConta()).claim("saldo", cliente.getSaldo())
					.claim("categoriaConta", cliente.getCategoriaConta()).claim("contaativa", cliente.isContaativa())
					.setExpiration(new Date(System.currentTimeMillis() + 86400000)).signWith(chaveSecreta).compact();
			return token;
		} else {
			return null;
		}
	}

	public Double visualizarSaldo(String cpfCliente) {
		return jdbcTemplateDaoImpl.viewSaldo(cpfCliente);
	}

	private boolean isValidTipoConta(String tipoConta) {
		return tipoConta.equalsIgnoreCase("corrente") || tipoConta.equalsIgnoreCase("poupanca");
	}

	private boolean isValidCategoriaConta(String categoriaConta) {
		return categoriaConta.equalsIgnoreCase("comum") || categoriaConta.equalsIgnoreCase("super")
				|| categoriaConta.equalsIgnoreCase("premium");
	}

	public void aplicarTaxaOuRendimentoDiario(String tipoConta, String categoriaConta, Cliente contaCorrente) {
		double saldo = contaCorrente.getSaldo();
		if ("corrente".equalsIgnoreCase(tipoConta)) {
			switch (categoriaConta) {
			case "Comum":
				contaCorrente.setSaldo(saldo - (12.0 / 30));
				break;
			case "Super":
				contaCorrente.setSaldo(saldo - (8.0 / 30));
				break;
			case "Premium":
				break;
			default:
				throw new IllegalArgumentException("Categoria de conta corrente não reconhecida: " + categoriaConta);
			}
		} else if ("poupança".equalsIgnoreCase(tipoConta)) {
			switch (categoriaConta) {
			case "Comum":
				contaCorrente.setSaldo(saldo + (saldo * 0.005 / 30));
				break;
			case "Super":
				contaCorrente.setSaldo(saldo + (saldo * 0.007 / 30));
				break;
			case "Premium":
				contaCorrente.setSaldo(saldo + (saldo * 0.009 / 30));
				break;
			default:
				throw new IllegalArgumentException("Categoria de conta poupança não reconhecida: " + categoriaConta);
			}
		} else {
			throw new IllegalArgumentException("Tipo de conta não reconhecido: " + tipoConta);
		}
	}

	public String alterarSenha(String token, String novaSenha) {
		return jdbcTemplateDaoImpl.alterarSenha(token, novaSenha);
	}

	public void ativarConta(String cpfCliente) {
		jdbcTemplateDaoImpl.atualizarStatusConta(cpfCliente, true);
	}

	public void desativarConta(String cpfCliente) {
		jdbcTemplateDaoImpl.atualizarStatusConta(cpfCliente, false);
	}

	public void ajustarLimiteTransacoes(String cpfCliente, int novoLimite) {
		jdbcTemplateDaoImpl.atualizarLimiteTransacoes(cpfCliente, novoLimite);
	}

	private boolean isValidCPF(String cpf) {
		return CPF_PATTERN.matcher(cpf).matches();
	}

}