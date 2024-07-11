package com.ecua3d.email.event;

import com.ecua3d.email.model.enums.OrderStatus;

import java.util.List;

public record SendEmailToCompanyEvent(
        Integer quoteId,
        String name,
        String email,
        String phone,
        List<String> fileNames,
        Integer fileId,
        Integer qualityId,
        String comment,
        OrderStatus orderStatus) {
}
