package com.panda.authenService.config;

import com.panda.authenService.interceptor.GrpcAuthInterceptor;
import io.grpc.ServerInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {

    @Value("${auth.gateway.private-key}")
    private String privateKey;

    @Bean
    @GrpcGlobalServerInterceptor
    public ServerInterceptor grpcAuthInterceptor() throws Exception {
        return new GrpcAuthInterceptor(privateKey);
    }
} 