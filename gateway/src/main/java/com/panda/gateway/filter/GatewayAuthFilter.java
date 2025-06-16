package com.panda.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import java.security.PublicKey;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

@Component
public class GatewayAuthFilter extends AbstractGatewayFilterFactory<GatewayAuthFilter.Config> {

    private final PublicKey publicKey;

    public GatewayAuthFilter(@Value("${gateway.auth.public-key}") String publicKeyStr) throws Exception {
        super(Config.class);
        // Chuyển đổi public key từ string sang PublicKey object
        publicKeyStr = publicKeyStr.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        
        byte[] encoded = Base64.getDecoder().decode(publicKeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        this.publicKey = keyFactory.generatePublic(keySpec);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            try {
                // Tạo token với thời gian hiện tại
                String tokenWithTime = "GATEWAY_AUTH:" + System.currentTimeMillis();
                
                // Mã hóa token bằng public key của service
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                byte[] encryptedBytes = cipher.doFinal(tokenWithTime.getBytes());
                String encryptedToken = Base64.getEncoder().encodeToString(encryptedBytes);

                // Thêm token vào header
                exchange.getRequest().mutate()
                        .header("X-Gateway-Token", encryptedToken)
                        .build();

                return chain.filter(exchange);
            } catch (Exception e) {
                throw new RuntimeException("Failed to encrypt gateway token", e);
            }
        };
    }

    public static class Config {
        // Cấu hình nếu cần
    }
} 