package com.bancodigital.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.bancodigital.entity.Cliente;
import com.bancodigital.service.ClienteService;

@RestController 
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;

    @PostMapping("/create/account")
    public void addCliente(@RequestBody Cliente cliente) {
        clienteService.criarCliente(cliente.getCpf(), cliente.getNome(), cliente.getEndereco());
    }

    @GetMapping("/all")
    public List<Cliente> getAllCliente(){
        return clienteService.listarClientes();
    }
}