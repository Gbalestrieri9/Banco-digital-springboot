package com.bancodigital.model;

import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Cliente {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String cpf;
	private String nome;
	private String endereco;
	private Date data;
	private String senha;
	private String tipoConta;
	private double saldo;
	private String categoriaConta;
	private boolean contaativa;
	private int limitetransacoes;
	
	public Cliente(String cpf, String nome, String endereco, Date data,String senha,String tipoConta, double saldo,String categoriaConta,boolean contaativa,int limitetransacoes) {
		super();
		this.cpf = cpf;
		this.nome = nome;
		this.endereco = endereco;
		this.setData(data);
		this.setSenha(senha);
		this.setTipoConta(tipoConta);
		this.setSaldo(saldo);
		this.setCategoriaConta(categoriaConta);
		this.setContaativa(contaativa);
		this.setLimitetransacoes(limitetransacoes);
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

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public String getCategoriaConta() {
		return categoriaConta;
	}

	public void setCategoriaConta(String categoriaConta) {
		this.categoriaConta = categoriaConta;
	}

	public boolean isContaativa() {
		return contaativa;
	}

	public void setContaativa(boolean contaativa) {
		this.contaativa = contaativa;
	}

	public int getLimitetransacoes() {
		return limitetransacoes;
	}

	public void setLimitetransacoes(int limitetransacoes) {
		this.limitetransacoes = limitetransacoes;
	}
}
