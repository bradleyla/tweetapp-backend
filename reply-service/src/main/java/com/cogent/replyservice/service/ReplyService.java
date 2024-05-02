package com.cogent.replyservice.service;

import com.cogent.replyservice.entity.Reply;
import com.cogent.replyservice.payload.ReplyRequest;

import java.util.List;

public interface ReplyService {
    public List<ReplyRequest> getAllRepliesByTweet(String username, Long tweetId);
    public ReplyRequest replyToTweet(String username, Long tweetId, Reply reply);
    public Reply likeReply(String username, Long replyId);
    public ReplyRequest getReplyRequestFromReply(Reply reply);



}
