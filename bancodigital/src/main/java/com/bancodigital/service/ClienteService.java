package com.bancodigital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import com.bancodigital.entity.Cliente;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void criarCliente(String cpf, String nome, String endereco) {
        jdbcTemplate.update("CALL criar_cliente(?, ?, ?)", cpf, nome, endereco);
    }

    public List<Cliente> listarClientes() {
        return jdbcTemplate.query("CALL listar_clientes()", new ClienteResultSetExtractor());
    }

    private static class ClienteResultSetExtractor implements ResultSetExtractor<List<Cliente>> {
        @Override
        public List<Cliente> extractData(ResultSet rs) throws SQLException {
            List<Cliente> clientes = new ArrayList<>();
            while (rs.next()) {
                String cpf = rs.getString("cpf");
                String nome = rs.getString("nome");
                String endereco = rs.getString("endereco");
                
                Cliente cliente = new Cliente(cpf, nome, endereco);
                cliente.setId(rs.getLong("id"));
                clientes.add(cliente);
            }
            return clientes;
        }
    }
}