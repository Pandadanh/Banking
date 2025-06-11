package com.panda.authenService.interceptor;

import io.grpc.*;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcAuthInterceptor implements ServerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(GrpcAuthInterceptor.class);
    private final PrivateKey privateKey;

    public GrpcAuthInterceptor(@Value("${auth.gateway.private-key}") String privateKeyStr) throws Exception {
        logger.info("Bắt đầu khởi tạo private key cho gRPC interceptor");
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
            logger.info("Đã đọc private key thành công với định dạng PKCS#8");
        } catch (Exception e) {
            logger.warn("Không thể đọc private key với định dạng PKCS#8, thử với PKCS#1: {}", e.getMessage());
            // Nếu không được, thử đọc như PKCS#1
            try {
                org.bouncycastle.asn1.pkcs.RSAPrivateKey rsaPrivateKey = org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance(encoded);
                RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(rsaPrivateKey.getModulus(), rsaPrivateKey.getPrivateExponent());
                tempKey = keyFactory.generatePrivate(keySpec);
                logger.info("Đã đọc private key thành công với định dạng PKCS#1");
            } catch (Exception ex) {
                logger.error("Không thể đọc private key với cả hai định dạng: {}", ex.getMessage());
                throw new IllegalArgumentException("Không thể đọc private key. Vui lòng kiểm tra định dạng key.", ex);
            }
        }
        this.privateKey = tempKey;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String gatewayToken = headers.get(Metadata.Key.of("x-gateway-token", Metadata.ASCII_STRING_MARSHALLER));
        
        if (gatewayToken == null) {
            logger.warn("Không tìm thấy x-gateway-token trong header");
            call.close(Status.UNAUTHENTICATED.withDescription("Missing gateway token"), new Metadata());
            return new ServerCall.Listener<ReqT>() {};
        }

        try {
            logger.debug("Bắt đầu giải mã token: {}", gatewayToken);
            // Giải mã token bằng private key
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(gatewayToken));
            String decryptedToken = new String(decryptedBytes);
            logger.debug("Token sau khi giải mã: {}", decryptedToken);

            // Kiểm tra token có phải từ Gateway không
            if (!decryptedToken.equals("GATEWAY_AUTH")) {
                logger.warn("Token không hợp lệ: {}", decryptedToken);
                call.close(Status.UNAUTHENTICATED.withDescription("Invalid gateway token"), new Metadata());
                return new ServerCall.Listener<ReqT>() {};
            }

            logger.info("Xác thực token thành công");
            return next.startCall(call, headers);
        } catch (Exception e) {
            logger.error("Lỗi khi xử lý token: {}", e.getMessage(), e);
            call.close(Status.UNAUTHENTICATED.withDescription("Failed to validate gateway token"), new Metadata());
            return new ServerCall.Listener<ReqT>() {};
        }
    }
} 