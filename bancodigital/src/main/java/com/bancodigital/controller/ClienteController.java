package com.bancodigital.controller;

import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bancodigital.model.Cliente;
import com.bancodigital.usecase.ClienteService;

@RestController 
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;

    @PostMapping("/create/account")
    public void addCliente(@RequestBody Cliente cliente) {
        Date dataSql = new Date(cliente.getData().getTime());
        clienteService.criarCliente(cliente.getCpf(), cliente.getNome(), cliente.getEndereco(), dataSql, cliente.getSenha(),cliente.getTipoConta(),cliente.getSaldo());
    }

    @GetMapping("/all")
    public List<Cliente> getAllCliente(){
        return clienteService.listarClientes();
    }
    
    
}