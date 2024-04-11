package com.bancodigital.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bancodigital.dao.impl.JdbcTemplateDaoImpl;
import com.bancodigital.model.Cliente;
import com.bancodigital.utils.Constantes;

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
}