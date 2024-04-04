package com.bancodigital.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bancodigital.entity.Cliente;
import com.bancodigital.service.ClienteService;

@RestController 
public class ClienteController {
	
	private ClienteService clienteService = new ClienteService();

	@PostMapping("/create/acount")
	public void addCliente(@RequestBody Cliente cliente) {
		clienteService.addCliente(cliente.getCpf(),cliente.getNome(),cliente.getEndereco());
	}
	
	@GetMapping("/all")
	public ArrayList<Cliente> getAllCliente(){
		
		return clienteService.getClientes();
	}
}
