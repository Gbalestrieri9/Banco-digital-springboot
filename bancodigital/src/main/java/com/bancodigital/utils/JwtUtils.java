package com.bancodigital.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.sql.Date;

import com.bancodigital.dto.JwtData;

public class JwtUtils {

    public static JwtData decodeToken(String token) {
        Key chaveSecreta = JwtConfig.getChaveSecreta();

        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(chaveSecreta)
                    .build()
                    .parseClaimsJws(token);
            Claims claims = jws.getBody();

            JwtData jwtData = new JwtData();
            jwtData.setCpf(claims.get("cpf", String.class));
            jwtData.setNome(claims.get("nome", String.class));
            jwtData.setEndereco(claims.get("endereco", String.class));
            jwtData.setTipoConta(claims.get("tipoConta", String.class));
            jwtData.setSaldo(claims.get("saldo", Double.class));
            jwtData.setCategoriaConta(claims.get("categoriaConta", String.class));
            jwtData.setContaativa(claims.get("contaativa", Boolean.class));

            return jwtData;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}