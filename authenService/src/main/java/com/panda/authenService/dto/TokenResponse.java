package com.panda.authenService.dto;

import lombok.Data;

@Data
public class TokenResponse {
    private String token;
    private String type = "Bearer";

    public TokenResponse(String token) {
        this.token = token;
    }

    public TokenResponse(String token, String type) {
        this.token = token;
        this.type = type;
    }
}