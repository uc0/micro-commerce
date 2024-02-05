package com.microcommerce.user.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class OrderController {

    @GetMapping
    public String testUSerServiceApi() {
        return "유저 서비스입니다.";
    }

}
