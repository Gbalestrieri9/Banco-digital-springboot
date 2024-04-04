package com.bancodigital.repository;

import java.util.ArrayList;

import com.bancodigital.entity.Cliente;

public class ClienteRepository {
	
	ArrayList<Cliente> listaClientes = new ArrayList<>();
	
	public void save(Cliente cliente) {
		listaClientes.add(cliente);
	}
	
	public ArrayList<Cliente> listAll(){
		return listaClientes;
	}

}
