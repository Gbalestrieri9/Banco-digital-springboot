package com.bancodigital.usecase;

import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class ClienteService {
    
    @Autowired
    private JdbcTemplateDaoImpl jdbcTemplateDaoImpl;

    public void criarCliente(String cpf, String nome, String endereco, Date data , String senha, String tipoConta, double saldo) {
    	jdbcTemplateDaoImpl.criarCliente(cpf, nome, endereco, data, senha, tipoConta, saldo);
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
        } 
        else if (clienteOrigem.getSaldo() < valor) {
        	mensagem = Constantes.MSG_TRANSFERENCIA_SALDO_INSUFICIENTE;
        }
        else {
        	mensagem = Constantes.MSG_TRANSFERENCIA_ERRO;
        }
		return mensagem;
    }
    
    public String login(String cpf, String senha) {
        Cliente cliente = jdbcTemplateDaoImpl.buscarClientePorCpf(cpf);
        if (cliente != null && cliente.getSenha().equals(senha)) {
     
            Key chaveSecreta = JwtConfig.getChaveSecreta();
       
            String token = Jwts.builder()
                    .claim("cpf", cliente.getCpf())
                    .claim("nome", cliente.getNome())
                    .claim("endereco", cliente.getEndereco())
                    .claim("data", cliente.getData())
                    .claim("tipoConta", cliente.getTipoConta())
                    .claim("saldo", cliente.getSaldo())
                    .setExpiration(new Date(System.currentTimeMillis() + 86400000)) 
                    .signWith(chaveSecreta)
                    .compact();
            return token;
        } else {
            return null;
        }
    }
}