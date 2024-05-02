package com.cogent.replyservice.external;

import com.cogent.replyservice.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="USER-SERVICE/api/v1.0/tweets/auth")
public interface UserService {
    @GetMapping("/user/search/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username);

    @GetMapping("/user/search/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id);
}
