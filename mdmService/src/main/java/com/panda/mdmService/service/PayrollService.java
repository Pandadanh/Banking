package com.panda.mdmService.service;

import com.panda.mdmService.dto.PayrollCreateRequest;
import com.panda.mdmService.dto.PayrollDto;
import com.panda.mdmService.dto.PayrollUpdateRequest;
import com.panda.mdmService.model.Payroll;
import com.panda.mdmService.repository.PayrollRepository;
import com.panda.mdmService.util.PayrollMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayrollService {
    private final PayrollRepository payrollRepository;

    public Flux<PayrollDto> getAll() {
        return payrollRepository.findAll().map(PayrollMapper::toDto);
    }

    public Mono<PayrollDto> getById(UUID id) {
        return payrollRepository.findById(id).map(PayrollMapper::toDto);
    }

    public Mono<PayrollDto> create(PayrollCreateRequest req) {
        Payroll payroll = PayrollMapper.fromCreateRequest(req);
        payroll.setId(UUID.randomUUID());
        return payrollRepository.save(payroll).map(PayrollMapper::toDto);
    }

    public Mono<PayrollDto> update(UUID id, PayrollUpdateRequest req) {
        return payrollRepository.findById(id)
                .flatMap(payroll -> {
                    PayrollMapper.updateFromRequest(payroll, req);
                    return payrollRepository.save(payroll);
                })
                .map(PayrollMapper::toDto);
    }

    public Mono<Void> delete(UUID id) {
        return payrollRepository.deleteById(id);
    }
} 