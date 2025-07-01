package com.panda.mdmService.util;

import com.panda.mdmService.model.CreditProfile;
import com.panda.mdmService.dto.CreditProfileDto;
import com.panda.mdmService.dto.CreditProfileCreateRequest;
import com.panda.mdmService.dto.CreditProfileUpdateRequest;

public class CreditProfileMapper {
    public static CreditProfileDto toDto(CreditProfile creditProfile) {
        if (creditProfile == null) return null;
        CreditProfileDto dto = new CreditProfileDto();
        dto.setId(creditProfile.getId());
        dto.setCustomerId(creditProfile.getCustomerId());
        dto.setCreditScore(creditProfile.getCreditScore());
        dto.setRiskLevel(creditProfile.getRiskLevel());
        dto.setNote(creditProfile.getNote());
        return dto;
    }

    public static CreditProfile fromCreateRequest(CreditProfileCreateRequest req) {
        CreditProfile creditProfile = new CreditProfile();
        creditProfile.setCustomerId(req.getCustomerId());
        creditProfile.setCreditScore(req.getCreditScore());
        creditProfile.setRiskLevel(req.getRiskLevel());
        creditProfile.setNote(req.getNote());
        return creditProfile;
    }

    public static void updateFromRequest(CreditProfile creditProfile, CreditProfileUpdateRequest req) {
        if (req.getCustomerId() != null) creditProfile.setCustomerId(req.getCustomerId());
        if (req.getCreditScore() != null) creditProfile.setCreditScore(req.getCreditScore());
        if (req.getRiskLevel() != null) creditProfile.setRiskLevel(req.getRiskLevel());
        if (req.getNote() != null) creditProfile.setNote(req.getNote());
    }
} 