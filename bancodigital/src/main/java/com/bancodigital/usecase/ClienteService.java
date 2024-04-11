package com.bancodigital.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bancodigital.dao.impl.JdbcTemplateDaoImpl;
import com.bancodigital.model.Cliente;

import java.sql.Date;
import java.util.List;

@Service
public class ClienteService {
    
    @Autowired
    private JdbcTemplateDaoImpl jdbcTemplateDaoImpl;

    public void criarCliente(String cpf, String nome, String endereco, Date data , String senha, String tipoConta) {
    	jdbcTemplateDaoImpl.criarCliente(cpf, nome, endereco, data, senha, tipoConta);
    }

    public List<Cliente> listarClientes() {
    	return jdbcTemplateDaoImpl.listarClientes();
    }
}