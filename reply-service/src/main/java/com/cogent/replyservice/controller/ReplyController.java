package com.cogent.replyservice.controller;

import com.cogent.replyservice.entity.Reply;
import com.cogent.replyservice.payload.ReplyRequest;
import com.cogent.replyservice.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1.0/replies")
public class ReplyController {
    @Autowired
    private ReplyService replyService;

    @GetMapping("/{username}/{tweetId}")
    public ResponseEntity<List<ReplyRequest>> getAllReplies(
            @PathVariable("username") String username,
            @PathVariable("tweetId") Long id) {
        var data = replyService.getAllRepliesByTweet(username, id);
        if (data == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @PostMapping("/{username}/{tweetId}")
    public ResponseEntity<ReplyRequest> replyToTweet(
            @PathVariable("username") String username,
            @PathVariable("tweetId") Long id, @RequestBody Reply reply) {
        var data = replyService.replyToTweet(username, id, reply);
        if (data == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    // Username is the person who liked it, not the person who owns the reply
    @PutMapping("/{username}/{replyId}/like")
    public ResponseEntity<Reply> likeTweet(
            @PathVariable("username") String username,
            @PathVariable("replyId") Long replyId
    ) {
        Reply data = replyService.likeReply(username, replyId);
        return ResponseEntity.ok(data);
    }
}
