package com.cogent.replyservice.service.impl;

import com.cogent.replyservice.entity.Reply;
import com.cogent.replyservice.external.TweeterService;
import com.cogent.replyservice.external.UserService;
import com.cogent.replyservice.model.Tweeter;
import com.cogent.replyservice.model.User;
import com.cogent.replyservice.payload.ReplyRequest;
import com.cogent.replyservice.repository.ReplyRepository;
import com.cogent.replyservice.service.ReplyService;
import feign.FeignException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private TweeterService tweetService;

    @Autowired
    private UserService userService;


    @Override
    public List<ReplyRequest> getAllRepliesByTweet(String username, Long tweetId) {
        List<Reply> replyList = replyRepository.findAllByTweetId(tweetId);
        return replyList.stream()
                .filter(Objects::nonNull)
                .map(this::getReplyRequestFromReply)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public ReplyRequest replyToTweet(String username, Long tweetId, Reply reply) {
        Tweeter tweeter = tweetService.getTweetById(tweetId).getBody();
        if (tweeter == null) {
            // Handle the case where the tweet is not found
            throw new RuntimeException("Tweet not found");
        }
        User user = userService.getUserByUsername(username).getBody();
        if (user == null) {
            // Handle the case where the user is not found
            throw new RuntimeException("User not found");
        }

        // Create ReplyData object for frontend response
        ReplyRequest replyRequest = new ReplyRequest();
        replyRequest.setTweet(tweeter);
        replyRequest.setUser(user);
        replyRequest.setTimestamp(LocalDateTime.now());
        replyRequest.setLikeCount(0L);

        // Create Reply object for database storage
        Reply replyForDb = new Reply();
        replyForDb.setTweetId(tweetId);
        replyForDb.setUserId(user.getUserId());
        replyForDb.setReplyContent(reply.getReplyContent());
        replyForDb.setTag(reply.getTag());
        replyForDb.setTimestamp(LocalDateTime.now());
        replyForDb.setLikeCount(0L);

        replyRepository.save(replyForDb);

        return replyRequest;

    }

    @Override
    public Reply likeReply(String username, Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("This shouldn't happen from the frontend"));
        reply.setLikeCount(reply.getLikeCount() + 1);
        return replyRepository.save(reply);
    }

    @Override
    public ReplyRequest getReplyRequestFromReply(Reply reply) {

        ResponseEntity<User> user;
        ResponseEntity<Tweeter> tweet;
        try {
            user = userService.getUserById(reply.getUserId());
        } catch (FeignException.FeignClientException ex) {
            log.error("User not found -- continuing");
            return null;
        } catch (Exception ex) {
            log.error("Unknown exception");
            log.error(ex);
            return null;
        }
        try {
            tweet = tweetService.getTweetById(reply.getTweetId());
        } catch (FeignException.FeignClientException ex) {
            log.error("Tweet not found -- continuing");
            return null;
        }

        return ReplyRequest.builder()
                .replyId(reply.getReplyId())
                .replyContent(reply.getReplyContent())
                .tag(reply.getTag())
                .timestamp(reply.getTimestamp())
                .likeCount(reply.getLikeCount())
                .tweet(tweet.getBody())
                .user(user.getBody())
                .build();

    }
}
