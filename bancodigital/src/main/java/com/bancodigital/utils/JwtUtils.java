package com.bancodigital.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.sql.Date;

import com.bancodigital.dto.JwtData;

public class JwtUtils {

    public static JwtData decodeToken(String token) {
        // Obtém a chave secreta da classe JwtConfig
        Key chaveSecreta = JwtConfig.getChaveSecreta();

        try {
            // Decodifica o token JWT
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(chaveSecreta)
                    .build()
                    .parseClaimsJws(token);
            Claims claims = jws.getBody();

            // Extrai os dados do token
            JwtData jwtData = new JwtData();
            jwtData.setCpf(claims.get("cpf", String.class));
            jwtData.setNome(claims.get("nome", String.class));
            jwtData.setEndereco(claims.get("endereco", String.class));
            jwtData.setTipoConta(claims.get("tipoConta", String.class));
            jwtData.setSaldo(claims.get("saldo", Double.class));
            jwtData.setCategoriaConta(claims.get("categoriaConta", String.class));

            return jwtData;

        } catch (Exception e) {
            // Se ocorrer um erro ao decodificar o token, imprime e retorna null
            e.printStackTrace();
            return null;
        }
    }
    
//    public static JwtData encodeToken(String token) {
//    	return null;
//    }
}