package com.microcommerce.webnotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroCommerceWebNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroCommerceWebNotificationApplication.class, args);
    }

}
