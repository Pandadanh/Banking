package com.panda.mdmService.util;

import com.panda.mdmService.model.LoginHistory;
import com.panda.mdmService.dto.LoginHistoryDto;
import com.panda.mdmService.dto.LoginHistoryCreateRequest;
import com.panda.mdmService.dto.LoginHistoryUpdateRequest;

public class LoginHistoryMapper {
    public static LoginHistoryDto toDto(LoginHistory loginHistory) {
        if (loginHistory == null) return null;
        LoginHistoryDto dto = new LoginHistoryDto();
        dto.setId(loginHistory.getId());
        dto.setUserId(loginHistory.getUserId());
        dto.setLoginTime(loginHistory.getLoginTime());
        dto.setIpAddress(loginHistory.getIpAddress());
        dto.setDevice(loginHistory.getDevice());
        return dto;
    }

    public static LoginHistory fromCreateRequest(LoginHistoryCreateRequest req) {
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setUserId(req.getUserId());
        loginHistory.setLoginTime(req.getLoginTime());
        loginHistory.setIpAddress(req.getIpAddress());
        loginHistory.setDevice(req.getDevice());
        return loginHistory;
    }

    public static void updateFromRequest(LoginHistory loginHistory, LoginHistoryUpdateRequest req) {
        if (req.getUserId() != null) loginHistory.setUserId(req.getUserId());
        if (req.getLoginTime() != null) loginHistory.setLoginTime(req.getLoginTime());
        if (req.getIpAddress() != null) loginHistory.setIpAddress(req.getIpAddress());
        if (req.getDevice() != null) loginHistory.setDevice(req.getDevice());
    }
} 