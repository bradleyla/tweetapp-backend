package com.cogent.replyservice.repository;

import com.cogent.replyservice.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByTweetId(Long tweetId);

}
