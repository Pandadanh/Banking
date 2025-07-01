package com.panda.mdmService.service;

import com.panda.mdmService.dto.NotificationCreateRequest;
import com.panda.mdmService.dto.NotificationDto;
import com.panda.mdmService.dto.NotificationUpdateRequest;
import com.panda.mdmService.model.Notification;
import com.panda.mdmService.repository.NotificationRepository;
import com.panda.mdmService.util.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public Flux<NotificationDto> getAll() {
        return notificationRepository.findAll().map(NotificationMapper::toDto);
    }

    public Mono<NotificationDto> getById(UUID id) {
        return notificationRepository.findById(id).map(NotificationMapper::toDto);
    }

    public Mono<NotificationDto> create(NotificationCreateRequest req) {
        Notification notification = NotificationMapper.fromCreateRequest(req);
        notification.setId(UUID.randomUUID());
        return notificationRepository.save(notification).map(NotificationMapper::toDto);
    }

    public Mono<NotificationDto> update(UUID id, NotificationUpdateRequest req) {
        return notificationRepository.findById(id)
                .flatMap(notification -> {
                    NotificationMapper.updateFromRequest(notification, req);
                    return notificationRepository.save(notification);
                })
                .map(NotificationMapper::toDto);
    }

    public Mono<Void> delete(UUID id) {
        return notificationRepository.deleteById(id);
    }
} 