package com.panda.gateway.interceptor;

import io.grpc.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.crypto.Cipher;
import java.security.PublicKey;
import java.util.Base64;

public class GrpcClientAuthInterceptor implements ClientInterceptor {

    @Autowired
    private PublicKey publicKey;

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                try {
                    // Mã hóa token bằng public key
                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                    byte[] encryptedBytes = cipher.doFinal("GATEWAY_AUTH".getBytes());
                    String encryptedToken = Base64.getEncoder().encodeToString(encryptedBytes);

                    // Thêm token vào header
                    headers.put(Metadata.Key.of("x-gateway-token", Metadata.ASCII_STRING_MARSHALLER), encryptedToken);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to encrypt gateway token", e);
                }

                super.start(responseListener, headers);
            }
        };
    }
} 