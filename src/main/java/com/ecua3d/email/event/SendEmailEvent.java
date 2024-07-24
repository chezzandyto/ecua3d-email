package com.ecua3d.email.event;

import com.ecua3d.email.model.enums.OrderStatus;
import java.util.List;

public record SendEmailEvent(Integer quoteId, String to, String name, List<String> fileNames, OrderStatus orderStatus) {
}
