package com.bancodigital.entity;

import java.util.Date;

public class Cliente {

	private String cpf;
	private String nome;
	//private Date dataNascimento;
	private String endereco;
	
	public Cliente(String cpf, String nome, String endereco) {
		this.cpf = cpf;
		this.nome = nome;
		//this.dataNascimento = dataNascimento;
		this.endereco = endereco;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

//	public Date getDataNascimento() {
//		return dataNascimento;
//	}
//
//	public void setDataNascimento(Date dataNascimento) {
//		this.dataNascimento = dataNascimento;
//	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
}
