package com.panda.authenService.service;

import com.panda.authenService.proto.AuthServiceGrpc;
import com.panda.authenService.proto.TokenRequest;
import com.panda.authenService.proto.TokenResponse;
import com.panda.authenService.proto.UserInfo;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

@GrpcService
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    @Override
    public void validateToken(TokenRequest request, StreamObserver<TokenResponse> responseObserver) {
        // TODO: Implement real token validation logic
        // For now, just return true for testing
        TokenResponse response = TokenResponse.newBuilder()
                .setValid(true)
                .setMessage("Token is valid")
                .setUserInfo(UserInfo.newBuilder()
                        .setUserId("1")
                        .setUsername("testuser")
                        .addRoles("USER")
                        .build())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
} 