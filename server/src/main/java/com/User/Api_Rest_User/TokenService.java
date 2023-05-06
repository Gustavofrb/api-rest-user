package com.User.Api_Rest_User;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class TokenService {
    @Value("${security.jwt.token.secret-key}")
    private String expiration;

    @Value("${security.jwt.token.expire-length}")
    private String secret;

    public String gerarToken(User user) {
        Date agora = new Date();
        Date expira = new Date(agora.getTime() + Long.parseLong(expiration));

        return JWT.create()
                .withIssuer("User")
                .withSubject(user.getUsername())
                .withClaim("id", (Boolean) user.getId())
                .withExpiresAt(expira)
                .sign(Algorithm.HMAC256(secret));
    }
		
	}
    
