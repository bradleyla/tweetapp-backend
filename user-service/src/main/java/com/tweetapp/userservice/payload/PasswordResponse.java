package com.tweetapp.userservice.payload;

import com.tweetapp.userservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResponse {
    private String message;
    private User user;
}

