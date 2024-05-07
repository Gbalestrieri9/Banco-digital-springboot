package com.bancodigital.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferenciaDTO {
	private String cpfDestino;
	private double valor;
	
	public String getCpfDestino() {
		return cpfDestino;
	}
	public void setCpfDestino(String cpfDestino) {
		this.cpfDestino = cpfDestino;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	
}