package com.cogent.replyservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tweets")
public class Tweeter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tweetId;
    @Column(name="user_id")
    private Long userId;
    @Column(nullable = false, name="tweet_content")
    private String message;
    private String tag;
    private LocalDateTime createdAt;
    @Column(name="like_count")
    private Long likes;
}
