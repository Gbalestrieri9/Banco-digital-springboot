package com.bancodigital.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bancodigital.dao.JdbcTemplateDao;
import com.bancodigital.model.CartaoCredito;
import com.bancodigital.model.Cliente;

import java.sql.Date;
import java.util.List;

@Service
public class JdbcTemplateDaoImpl implements JdbcTemplateDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void criarCliente(String cpf, String nome, String endereco, Date data , String senha, String tipoConta, double saldo, String categoriaConta) {
        jdbcTemplate.update("CALL criar_cliente(?, ?, ?, ?, ?, ?, ?, ?)"
        		, cpf, nome, endereco, new java.sql.Date(data.getTime()), senha,tipoConta, saldo, categoriaConta);
    }

    public List<Cliente> listarClientes() {
        return jdbcTemplate.query("SELECT * FROM listar_clientes()", (rs, rowNum) -> {
            Cliente cliente = new Cliente(
                rs.getString("cpf"),
                rs.getString("nome"),
                rs.getString("endereco"),
                rs.getDate("data"),
                rs.getString("senha"),
                rs.getString("tipoConta"),
                rs.getDouble("saldo"),
                rs.getString("categoriaConta"),
                rs.getBoolean("contaativa"),
                rs.getInt("limitetransacoes")
            );
            return cliente;
        });
    }
    
    public Cliente buscarClientePorCpf(String cpf) {
        String query = "SELECT * FROM cliente WHERE cpf = ?";
        @SuppressWarnings("deprecation")
		List<Cliente> clientes = jdbcTemplate.query(query, new Object[]{cpf}, (rs, rowNum) -> {
            return new Cliente(
                rs.getString("cpf"),
                rs.getString("nome"),
                rs.getString("endereco"),
                rs.getDate("data"),
                rs.getString("senha"),
                rs.getString("tipoConta"),
                rs.getDouble("saldo"),
                rs.getString("categoriaConta"),
                rs.getBoolean("contaativa"),
                rs.getInt("limitetransacoes")
            );
        });

        if (!clientes.isEmpty()) {
            return clientes.get(0);
        } else {
            return null;
        }
    }

    public void atualizarSaldoCliente(String cpf, double novoSaldo) {
        String query = "UPDATE cliente SET saldo = ? WHERE cpf = ?";
        jdbcTemplate.update(query, novoSaldo, cpf);
    }
    
    @SuppressWarnings("deprecation")
    public Double viewSaldo(String cpfCliente) {
        return jdbcTemplate.queryForObject("SELECT saldo FROM listar_clientes() WHERE cpf = ?",
                new Object[]{cpfCliente}, Double.class);
    }

    public String alterarSenha(String cpf, String novaSenha) {
        String updateQuery = "UPDATE cliente SET senha = ? WHERE cpf = ?";
        jdbcTemplate.update(updateQuery, novaSenha, cpf);
        return "Senha alterada com sucesso!";
    }

	public void criarCartaoDeCredito(String cpf, double limiteCartao) {
		String sql = "INSERT INTO CartaoCredito (limite_credito, valor_fatura, cliente_id) " +
                "VALUES (?, 0, (SELECT id FROM Cliente WHERE cpf = ?))";
		jdbcTemplate.update(sql,limiteCartao,cpf);
	}
	
	@SuppressWarnings("deprecation")
	public CartaoCredito consultarCartaoDeCredito(String cpfCliente) {
		String sql = "SELECT * FROM CartaoCredito WHERE cliente_id = (SELECT id FROM Cliente WHERE cpf = ?)";
		return jdbcTemplate.queryForObject(sql, new Object[]{cpfCliente}, (rs, rowNum) -> {
            CartaoCredito cartao = new CartaoCredito();
            cartao.setId(rs.getLong("id"));
            cartao.setLimiteCredito(rs.getDouble("limite_credito"));
            cartao.setValorFatura(rs.getDouble("valor_fatura"));
            return cartao;
        });
	}
	
	public void atualizarLimiteCartao(String cpfCliente, double novoLimite) {
	    String sql = "UPDATE CartaoCredito SET limite_credito = ? WHERE cliente_id = (SELECT id FROM Cliente WHERE cpf = ?)";
	    jdbcTemplate.update(sql, novoLimite, cpfCliente);
	}

	public void atualizarValorFatura(String cpfCliente, double novaFatura) {
	    String sql = "UPDATE CartaoCredito SET valor_fatura = ? WHERE cliente_id = (SELECT id FROM Cliente WHERE cpf = ?)";
	    jdbcTemplate.update(sql, novaFatura, cpfCliente);
	}
   
	
	public void atualizarStatusConta(String cpfCliente, boolean status) {
	    String sql = "UPDATE Cliente SET contaativa = ? WHERE cpf = ?";
	    jdbcTemplate.update(sql, status, cpfCliente);
	}
	
	public void atualizarLimiteTransacoes(String cpfCliente, int novoLimite) {
	    String sql = "UPDATE Cliente SET limitetransacoes = ? WHERE cpf = ?";
	    jdbcTemplate.update(sql, novoLimite, cpfCliente);
	}
	
	public void salvarApoliceViagem(Long cartaoCreditoId, Double valorApolice) {
	    String sql = "INSERT INTO ApoliceViagem (cartao_credito_id, valor_apolice) VALUES (?, ?)";
	    jdbcTemplate.update(sql, cartaoCreditoId, valorApolice);
	}

	public void salvarApoliceFraude(String cpfCliente, String detalhesApolice) {
	    Cliente cliente = buscarClientePorCpf(cpfCliente);
	    if (cliente != null) {
	        String sql = "INSERT INTO ApoliceFraude (cpf_cliente, detalhes) VALUES (?, ?)";
	        jdbcTemplate.update(sql, cpfCliente, detalhesApolice);
	    }
	}
	
	public Long buscarCartaoCreditoIdPorCpfCliente(String cpfCliente) {
	    String sql = "SELECT id FROM CartaoCredito WHERE cliente_id = (SELECT id FROM Cliente WHERE cpf = ?)";
	    try {
	        return jdbcTemplate.queryForObject(sql, Long.class, cpfCliente);
	    } catch (EmptyResultDataAccessException e) {
	        return null;
	    }
	}
}