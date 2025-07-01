package com.panda.mdmService.util;

import com.panda.mdmService.model.Notification;
import com.panda.mdmService.dto.NotificationDto;
import com.panda.mdmService.dto.NotificationCreateRequest;
import com.panda.mdmService.dto.NotificationUpdateRequest;

public class NotificationMapper {
    public static NotificationDto toDto(Notification notification) {
        if (notification == null) return null;
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setMessage(notification.getMessage());
        dto.setSentTime(notification.getSentTime());
        dto.setStatus(notification.getStatus());
        return dto;
    }

    public static Notification fromCreateRequest(NotificationCreateRequest req) {
        Notification notification = new Notification();
        notification.setUserId(req.getUserId());
        notification.setMessage(req.getMessage());
        notification.setSentTime(req.getSentTime());
        notification.setStatus(req.getStatus());
        return notification;
    }

    public static void updateFromRequest(Notification notification, NotificationUpdateRequest req) {
        if (req.getUserId() != null) notification.setUserId(req.getUserId());
        if (req.getMessage() != null) notification.setMessage(req.getMessage());
        if (req.getSentTime() != null) notification.setSentTime(req.getSentTime());
        if (req.getStatus() != null) notification.setStatus(req.getStatus());
    }
} 