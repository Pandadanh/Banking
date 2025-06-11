package com.panda.gateway.config;

import com.panda.gateway.interceptor.GrpcClientAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.grpc.ClientInterceptor;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;

@Configuration
public class GrpcClientConfig {

    @Bean
    @GrpcGlobalClientInterceptor
    public ClientInterceptor grpcClientAuthInterceptor() {
        return new GrpcClientAuthInterceptor();
    }
}