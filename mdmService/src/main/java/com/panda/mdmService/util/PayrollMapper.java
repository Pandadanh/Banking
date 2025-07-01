package com.panda.mdmService.util;

import com.panda.mdmService.model.Payroll;
import com.panda.mdmService.dto.PayrollDto;
import com.panda.mdmService.dto.PayrollCreateRequest;
import com.panda.mdmService.dto.PayrollUpdateRequest;

public class PayrollMapper {
    public static PayrollDto toDto(Payroll payroll) {
        if (payroll == null) return null;
        PayrollDto dto = new PayrollDto();
        dto.setId(payroll.getId());
        dto.setEmployeeId(payroll.getEmployeeId());
        dto.setPayDate(payroll.getPayDate());
        dto.setAmount(payroll.getAmount());
        dto.setNote(payroll.getNote());
        return dto;
    }

    public static Payroll fromCreateRequest(PayrollCreateRequest req) {
        Payroll payroll = new Payroll();
        payroll.setEmployeeId(req.getEmployeeId());
        payroll.setPayDate(req.getPayDate());
        payroll.setAmount(req.getAmount());
        payroll.setNote(req.getNote());
        return payroll;
    }

    public static void updateFromRequest(Payroll payroll, PayrollUpdateRequest req) {
        if (req.getEmployeeId() != null) payroll.setEmployeeId(req.getEmployeeId());
        if (req.getPayDate() != null) payroll.setPayDate(req.getPayDate());
        if (req.getAmount() != null) payroll.setAmount(req.getAmount());
        if (req.getNote() != null) payroll.setNote(req.getNote());
    }
} 