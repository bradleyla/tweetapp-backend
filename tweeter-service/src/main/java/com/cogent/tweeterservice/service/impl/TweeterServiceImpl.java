package com.cogent.tweeterservice.service.impl;

import com.cogent.tweeterservice.entity.Tweeter;
import com.cogent.tweeterservice.exception.CustomRuntimeException;
import com.cogent.tweeterservice.external.UserService;
import com.cogent.tweeterservice.model.User;
import com.cogent.tweeterservice.payload.TweeterResponse;
import com.cogent.tweeterservice.repository.TweeterRepository;
import com.cogent.tweeterservice.service.TweeterService;
import feign.FeignException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
@Log4j2
@Service
public class TweeterServiceImpl implements TweeterService {
    @Autowired
    private UserService userService;
    @Autowired
    private TweeterRepository tweetRepository;
    @Override
    public List<TweeterResponse> getAllTweets() {
        List<Tweeter> tweets = tweetRepository.findAll();
        return tweets.stream()
                .map(this::buildTweetResponseFromTweet
                ).filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<TweeterResponse> getAllTweetsByUsername(String username) {
        ResponseEntity<User> user = userService.getUserByUsername(username);
        if (user.getStatusCode() != HttpStatus.OK || !user.hasBody()) {
            throw new CustomRuntimeException("User not found", "USER_NOT_FOUND", 404);
        }
        List<Tweeter> tweets = tweetRepository.findAllByUserId(user.getBody().getUserId());
        return tweets.stream()
                .map(this::buildTweetResponseFromTweet
                ).filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<TweeterResponse> getAllTweetsByUserId(Long userId) {
        ResponseEntity<User> user = userService.getUserById(userId);
        if (user.getStatusCode() != HttpStatus.OK || !user.hasBody()) {
            throw new CustomRuntimeException("User not found", "USER_NOT_FOUND", 404);
        }
        List<Tweeter> tweets = tweetRepository.findAllByUserId(user.getBody().getUserId());
        return tweets.stream()
                .map(this::buildTweetResponseFromTweet
                ).filter(Objects::nonNull)
                .toList();
    }

    @Override
    public TweeterResponse getTweetById(Long tweetId) {
        Tweeter tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new CustomRuntimeException("Tweet not found", "TWEET_NOT_FOUND", 404));
        return buildTweetResponseFromTweet(tweet);
    }

    @Override
    public TweeterResponse postTweet(String username, Tweeter tweet) {

        User user = getUserByUsername(username);

        tweet.setUserId(user.getUserId());
        tweet.setCreatedAt(LocalDateTime.now());
        tweet.setLikes(0L);

        tweet = tweetRepository.save(tweet);

        return buildTweetResponseFromTweet(tweet);
    }

    @Override
    public TweeterResponse updateTweet(String username, Long id, Tweeter newTweet) {

        User user = getUserByUsername(username);
        Tweeter oldTweet = tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet not found"));

        if (!isTweetOwner(oldTweet.getUserId(), user)) {
            throw new RuntimeException("Cannot update a tweet that is not owned by you");
        }

        oldTweet.setMessage(newTweet.getMessage());
        oldTweet.setTag(newTweet.getTag());
        oldTweet.setCreatedAt(LocalDateTime.now());

        Tweeter tweet = tweetRepository.save(oldTweet);
        return buildTweetResponseFromTweet(tweet);
    }

    @Override
    public String deleteTweet(String username, Long id) {

        User user = getUserByUsername(username);

        Tweeter tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet not found"));

        if (!isTweetOwner(tweet.getUserId(), user)) {
            throw new RuntimeException("Cannot delete a tweet that is not owned by you");
        }
        // Delete
        tweetRepository.delete(tweet);
        return "Tweet Deleted Successfully";
    }

    @Override
    public TweeterResponse likeTweet(String username, Long id) {
        User user = getUserByUsername(username);
        Tweeter tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tweet not found"));

        tweet.setLikes(tweet.getLikes() + 1);
        tweet = tweetRepository.save(tweet);
        return buildTweetResponseFromTweet(tweet);
    }
    private User getUserByUsername(String username) {
        User user = null;
        try {
            user = userService.getUserByUsername(username).getBody();
            log.info("User retrieved: {}", username);
        } catch (Exception ex) {
            log.error("User could not be retrieved");
            log.error(ex);
        }
        if (user == null) {
            throw new CustomRuntimeException("User not found", "UserServiceError", 404);
        }
        return user;
    }
    private boolean isTweetOwner(Long tweetUserId, User user) {
        return (Objects.equals(tweetUserId, user.getUserId()));
    }
    private TweeterResponse buildTweetResponseFromTweet(Tweeter tweet) {
        ResponseEntity<User> user;
        try {
            user = userService.getUserById(tweet.getUserId());
        } catch (FeignException.FeignClientException ex) {
            log.error("User not found");
            return null;
        } catch (Exception ex) {
            log.error("Unknown exception");
            log.error(ex);
            return null;
        }

        return TweeterResponse.builder()
                .message(tweet.getMessage())
                .tag(tweet.getTag())
                .createdAt(tweet.getCreatedAt())
                .likes(tweet.getLikes())
                .tweetId(tweet.getTweetId())
                .user(user.getBody()).build();
    }
}
