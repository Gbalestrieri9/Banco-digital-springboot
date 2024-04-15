package com.bancodigital.utils;

public class Constantes {
	
	public static final String MSG_TRANSFERENCIA_SALDO_INSUFICIENTE = "Transferência não pode ser concluída. Saldo insuficiente.";
	public static final String MSG_TRANSFERENCIA_SUCESSO = "Transferência realizada com sucesso!";
	public static final String MSG_TRANSFERENCIA_ERRO = "Transferência não pode ser concluída. Verifique os dados informados.";
	public static final String MSG_CRIACAO_CLIENTE_TIPO_CONTA_ERRO = "Tipo de conta inválido. O tipo de conta deve ser 'corrente' ou 'poupanca'.";
	public static final String MSG_CRIACAO_CLIENTE_CATEGORIA_CONTA_ERRO = "Categoria de conta inválida. A categoria de conta deve ser 'comum', 'super' ou 'premium'.";
	public static final String MSG_CRIACAO_CLIENTE_SUCESSO = "Cliente criado com sucesso!";
	public static final String MSG_CARTAO_CREDITO_POUPANCA_ERRO = "Clientes com conta poupança não podem ter cartão de crédito.";
	public static final double LIMITE_MAXIMO_CARTAO_CREDITO = 0;
	public static final String MSG_CARTAO_CREDITO_LIMITE_ERRO = "Limite do cartão de crédito ultrapassa o limite máximo permitido.";
	public static final String MSG_CARTAO_CREDITO_CATEGORIA_ERRO = "Categoria de conta não reconhecida para criar o cartão de crédito.";
	public static final String MSG_CADASTRO_CPF_ERRO = "CPF inválido.";
}
