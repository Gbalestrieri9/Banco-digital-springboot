package com.bancodigital.dto;

public class JwtData {

    private String cpf;
    private String nome;
    private String endereco;
    private String tipoConta;
    private Double saldo;
    private String categoriaConta;
    private boolean contaativa;
    
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
	public String getTipoConta() {
		return tipoConta;
	}
	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
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

}
