package com.bancodigital.utils;

import java.util.regex.Pattern;

public class CPFUtil {

    // Constante para o padrão de CPF formatado
    private static final Pattern CPF_FORMATADO_PATTERN = Pattern.compile("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");

    // Constante para o padrão de CPF sem formatação
    private static final Pattern CPF_SEM_FORMATACAO_PATTERN = Pattern.compile("\\d{11}");

    // Método para verificar se o CPF é válido
    public static boolean isValidCPF(String cpf) {
        // Verifica se o CPF está formatado corretamente
        if (!CPF_FORMATADO_PATTERN.matcher(cpf).matches()) {
            return false;
        }

        // Remove a formatação do CPF
        String cpfSemFormatacao = cpf.replaceAll("[^0-9]", "");

        // Verifica se o CPF sem formatação possui 11 dígitos
        if (!CPF_SEM_FORMATACAO_PATTERN.matcher(cpfSemFormatacao).matches()) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int digito1 = calcularDigitoVerificador(cpfSemFormatacao.substring(0, 9));

        // Calcula o segundo dígito verificador
        int digito2 = calcularDigitoVerificador(cpfSemFormatacao.substring(0, 9) + digito1);

        // Verifica se os dígitos verificadores calculados são iguais aos dígitos verificadores informados
        return cpfSemFormatacao.endsWith(Integer.toString(digito1) + Integer.toString(digito2));
    }

    // Método para calcular o dígito verificador do CPF
    private static int calcularDigitoVerificador(String cpfParcial) {
        int soma = 0;
        int peso = cpfParcial.length() + 1;

        for (int i = 0; i < cpfParcial.length(); i++) {
            soma += Integer.parseInt(String.valueOf(cpfParcial.charAt(i))) * peso--;
        }

        int resto = soma % 11;
        int digito = 11 - resto;

        if (digito > 9) {
            return 0;
        }

        return digito;
    }
}
