package com.tweetapp.cloudgateway.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @PostMapping("/userServiceFallBack")
    public String userServiceFallBack() {
        return "User Service is down.";
    }

    @PostMapping("/tweetServiceFallBack")
    public String tweetServiceFallBack() {
        return "Tweet Service is down.";
    }

    @PostMapping("/replyServiceFallBack")
    public String replyServiceFallBack() {
        return "Reply Service is down.";
    }
}
