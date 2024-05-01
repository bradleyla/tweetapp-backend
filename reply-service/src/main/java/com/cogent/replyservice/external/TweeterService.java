package com.cogent.replyservice.external;

import com.cogent.replyservice.model.Tweeter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name="TWEETER-SERVICE/api/v1.0/tweets")
public interface TweeterService {
        @GetMapping("/id/{id}")
        public ResponseEntity<Tweeter> getTweetById(@PathVariable("id") Long tweetId);

}
