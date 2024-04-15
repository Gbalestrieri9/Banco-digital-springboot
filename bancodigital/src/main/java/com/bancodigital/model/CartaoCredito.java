package com.bancodigital.model;

public class CartaoCredito {
    private Long id;
    private Double limiteCredito;
    private Double valorFatura;

    public CartaoCredito() {}

    public CartaoCredito(Long id, Double limiteCredito, Double valorFatura) {
        this.id = id;
        this.limiteCredito = limiteCredito;
        this.valorFatura = valorFatura;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(Double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public Double getValorFatura() {
        return valorFatura;
    }

    public void setValorFatura(Double valorFatura) {
        this.valorFatura = valorFatura;
    }
}
