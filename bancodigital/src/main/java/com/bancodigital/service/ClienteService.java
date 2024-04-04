package com.bancodigital.service;

import java.util.ArrayList;

import com.bancodigital.entity.Cliente;
import com.bancodigital.repository.ClienteRepository;

public class ClienteService {

	private ClienteRepository clienteRepository = new ClienteRepository();
	
	public void addCliente(String cpf,String nome,String endereco) {
		Cliente cliente = new Cliente (cpf,nome,endereco);
		
		clienteRepository.save(cliente);
	}
	
	public ArrayList<Cliente> getClientes(){
		return clienteRepository.listAll();
	}
}
