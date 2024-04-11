package com.bancodigital.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteRepository {

    private Connection connection;

    // Construtor para estabelecer a conexão com o banco de dados
    public ClienteRepository() throws SQLException {
        // Configurações para conexão com o banco de dados
        String url = "jdbc:postgresql://localhost:5432/db_bancodigital";
        String user = "postgres";
        String password = "clebinho";

        // Estabelece a conexão
        connection = DriverManager.getConnection(url, user, password);
    }

    public void criarCliente(String cpf, String nome, String endereco, Date data, String senha, String tipoConta) throws SQLException {
        String sql = "{call criar_cliente(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(sql)) {
            statement.setString(1, cpf);
            statement.setString(2, nome);
            statement.setString(3, endereco);
            statement.setDate(4, data); // Define a data
            statement.setString(5, senha);
            statement.setString(6, tipoConta);
            statement.execute();
        }
    }

    public void listarClientes() throws SQLException {
        String sql = "SELECT * FROM listar_clientes_funcao()";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String cpf = resultSet.getString("cpf");
                String nome = resultSet.getString("nome");
                String endereco = resultSet.getString("endereco");
                System.out.println("ID: " + id + ", CPF: " + cpf + ", Nome: " + nome + ", Endereço: " + endereco);
            }
        }
    }

    // Método para fechar a conexão com o banco de dados
    public void fecharConexao() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
