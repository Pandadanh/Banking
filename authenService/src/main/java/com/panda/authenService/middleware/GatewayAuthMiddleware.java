package com.panda.authenService.middleware;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Base64;

@Component
public class GatewayAuthMiddleware implements HandlerInterceptor {

    private final PrivateKey privateKey;

    public GatewayAuthMiddleware(@Value("${auth.gateway.private-key}") String privateKeyStr) throws Exception {
        // Loại bỏ header và footer của key
        privateKeyStr = privateKeyStr
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        
        byte[] encoded = Base64.getDecoder().decode(privateKeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        
        PrivateKey tempKey;
        try {
            // Thử đọc như PKCS#8 trước
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            tempKey = keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            // Nếu không được, thử đọc như PKCS#1
            try {
                org.bouncycastle.asn1.pkcs.RSAPrivateKey rsaPrivateKey = org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance(encoded);
                RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(rsaPrivateKey.getModulus(), rsaPrivateKey.getPrivateExponent());
                tempKey = keyFactory.generatePrivate(keySpec);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Không thể đọc private key. Vui lòng kiểm tra định dạng key.", ex);
            }
        }
        this.privateKey = tempKey;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String gatewayToken = request.getHeader("X-Gateway-Token");
        
        if (gatewayToken == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        try {
            // Giải mã token bằng private key
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(gatewayToken));
            String decryptedToken = new String(decryptedBytes);

            // Tách token và thời gian
            String[] parts = decryptedToken.split(":");
            if (parts.length != 2) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            String token = parts[0];
            long tokenTime = Long.parseLong(parts[1]);
            long currentTime = System.currentTimeMillis();
            
            // Kiểm tra token có phải từ Gateway không và chưa hết hạn (15 phút)
            if (!token.equals("GATEWAY_AUTH") || (currentTime - tokenTime) > 15 * 60 * 1000) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Không cần xử lý gì thêm
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Không cần xử lý gì thêm
    }
} 