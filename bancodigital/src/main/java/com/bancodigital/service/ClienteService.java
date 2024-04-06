package com.bancodigital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.bancodigital.entity.Cliente;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void criarCliente(String cpf, String nome, String endereco) {
        jdbcTemplate.update("CALL criar_cliente(?, ?, ?)", cpf, nome, endereco);
    }

    public List<Cliente> listarClientes() {
        return jdbcTemplate.query("SELECT * FROM listar_clientes()", (rs, rowNum) -> {
            Cliente cliente = new Cliente(
                rs.getString("cpf"),
                rs.getString("nome"),
                rs.getString("endereco")
            );
            return cliente;
        });
    }
}