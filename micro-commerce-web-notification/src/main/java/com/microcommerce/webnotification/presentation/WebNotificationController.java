package com.microcommerce.webnotification.presentation;

import com.microcommerce.webnotification.application.WebNotificationService;
import com.microcommerce.webnotification.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
public class WebNotificationController {

    private final WebNotificationService webNotificationService;

    @GetMapping("/api/v1/notification/subscribe")
    public SseEmitter subscribe(@RequestHeader final HttpHeaders headers) {
        final long userId = HeaderUtil.getUserId(headers);
        return webNotificationService.connectNotification(userId);
    }

}
