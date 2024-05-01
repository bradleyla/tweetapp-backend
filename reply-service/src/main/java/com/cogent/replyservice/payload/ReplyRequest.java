package com.cogent.replyservice.payload;

import com.cogent.replyservice.model.Tweeter;
import com.cogent.replyservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyRequest {
    private Long replyId;
    private Tweeter tweet;
    private User user;
    private String replyContent;
    private String tag;
    private LocalDateTime timestamp;
    private Long likeCount;
}
