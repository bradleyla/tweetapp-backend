package com.cogent.tweeterservice.payload;

import com.cogent.tweeterservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TweeterResponse {
    private Long tweetId;

    private User user;

    private String message;

    private String tag;

    private LocalDateTime createdAt;
    private Long likes;
}
