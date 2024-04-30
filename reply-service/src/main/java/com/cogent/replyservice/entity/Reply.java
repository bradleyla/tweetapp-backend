package com.cogent.replyservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    private Long tweetId;

    private Long userId;

    @Column(nullable = false)
    private String replyContent;

    private String tag;

    private LocalDateTime timestamp;
    private Long likeCount;

}
