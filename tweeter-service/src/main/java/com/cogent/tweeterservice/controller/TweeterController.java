package com.cogent.tweeterservice.controller;

import com.cogent.tweeterservice.entity.Tweeter;
import com.cogent.tweeterservice.payload.TweeterResponse;
import com.cogent.tweeterservice.service.TweeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweeterController {
    @Autowired
    private TweeterService tweetService;

    // Get all tweets
    @GetMapping("/all")
    public ResponseEntity<List<TweeterResponse>> getAllTweets() {
        var data = tweetService.getAllTweets();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<TweeterResponse> getTweetById(@PathVariable("id") Long tweetId) {
        TweeterResponse tweet = tweetService.getTweetById(tweetId);
        if (tweet == null) {
            return new ResponseEntity<>(tweet, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tweet, HttpStatus.OK);
    }

    // Get tweets by user
    @GetMapping("/user/{username}")
    public ResponseEntity<List<TweeterResponse>> getAllTweetsByUser(@PathVariable("username") String username) {
        var data = tweetService.getAllTweetsByUsername(username);
        if (data == null) {
            return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    // post a tweet
    @PostMapping("/{username}/add")
    public ResponseEntity<TweeterResponse> postTweet(
            @PathVariable("username") String username,
            @RequestBody Tweeter tweet) {
        var data = tweetService.postTweet(username, tweet);
        if (data == null) {
            return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    // update a tweet
    @PutMapping("/{username}/update/{tweetId}")
    public ResponseEntity<TweeterResponse> updateTweet(
            @PathVariable("username") String username,
            @PathVariable("tweetId") Long id,
            @RequestBody Tweeter tweet) {
        var data = tweetService.updateTweet(username, id, tweet);
        return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
    }

    // Delete a tweet
    @DeleteMapping("/{username}/delete/{tweetId}")
    public ResponseEntity<String> deleteTweet(
            @PathVariable("username") String username,
            @PathVariable("tweetId") Long id) {
        String result = tweetService.deleteTweet(username, id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Like a tweet
    @PutMapping("/{username}/like/{tweetId}")
    public ResponseEntity<TweeterResponse> likeTweet(
            @PathVariable("username") String username,
            @PathVariable("tweetId") Long id) {
        TweeterResponse data = tweetService.likeTweet(username, id);
        if (data == null) {
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
