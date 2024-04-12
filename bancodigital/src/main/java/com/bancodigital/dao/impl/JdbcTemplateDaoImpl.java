package com.bancodigital.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bancodigital.dao.JdbcTemplateDao;
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
                rs.getString("categoriaConta")
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
                rs.getString("categoriaConta")
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
	public List<Cliente> viewSaldo(double saldo) {
        return jdbcTemplate.query("SELECT * FROM listar_clientes() WHERE saldo = ?", new Object[]{saldo}, (rs, rowNum) -> {
            Cliente cliente = new Cliente(
                rs.getString("cpf"),
                rs.getString("nome"),
                rs.getString("endereco"),
                rs.getDate("data"),
                rs.getString("senha"),
                rs.getString("tipoConta"),
                rs.getDouble("saldo"),
                rs.getString("categoriaConta")
            );
            return cliente;
        });
    }


}