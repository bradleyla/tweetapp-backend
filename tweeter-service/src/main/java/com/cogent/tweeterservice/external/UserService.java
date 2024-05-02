package com.cogent.tweeterservice.external;

import com.cogent.tweeterservice.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="USER-SERVICE/api/v1.0/auth")
public interface UserService {
    @GetMapping("/users/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id);

    @GetMapping("/users/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username);
}

