package com.panda.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
public class RsaConfig {
    
    @Bean
    public KeyPair rsaKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    @Bean
    public PublicKey publicKey(KeyPair keyPair) {
        return keyPair.getPublic();
    }

    @Bean
    public PrivateKey privateKey(KeyPair keyPair) {
        return keyPair.getPrivate();
    }
} 