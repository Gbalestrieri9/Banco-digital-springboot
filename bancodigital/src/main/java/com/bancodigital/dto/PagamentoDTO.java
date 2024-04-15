package com.bancodigital.dto;

public class PagamentoDTO {
    private double valor;
    private String tipoPagamento; 

    public PagamentoDTO() {}

    public PagamentoDTO(double valor, String tipoPagamento) {
        this.valor = valor;
        this.tipoPagamento = tipoPagamento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }
}
