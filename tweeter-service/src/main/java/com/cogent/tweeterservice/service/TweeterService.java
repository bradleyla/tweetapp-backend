package com.cogent.tweeterservice.service;

import com.cogent.tweeterservice.entity.Tweeter;
import com.cogent.tweeterservice.payload.TweeterResponse;

import java.util.List;

public interface TweeterService {
    public List<TweeterResponse> getAllTweets();
    public List<TweeterResponse> getAllTweetsByUsername(String username);
    public List<TweeterResponse> getAllTweetsByUserId(Long userId);
    public TweeterResponse getTweetById(Long tweetId);
    public TweeterResponse postTweet(String username, Tweeter tweet);
    public TweeterResponse updateTweet(String username, Long id, Tweeter newTweet);
    public String deleteTweet(String username, Long id);
    public TweeterResponse likeTweet(String username, Long id);
}
