package com.proyecto.integrado.vummy.security.jwt;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    private final Map<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    public void addTokenToBlacklist(String token, long expirationTime) {
        blacklistedTokens.put(token, expirationTime);
    }

    public boolean isTokenBlacklisted(String token) {
        Long expirationTime = blacklistedTokens.get(token);

        // Eliminar el token si ya expiró
        if (expirationTime != null && expirationTime < System.currentTimeMillis()) {
            blacklistedTokens.remove(token);
            return false;
        }

        return expirationTime != null;
    }
}