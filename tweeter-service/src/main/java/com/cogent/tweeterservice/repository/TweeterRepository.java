package com.cogent.tweeterservice.repository;

import com.cogent.tweeterservice.entity.Tweeter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TweeterRepository extends JpaRepository<Tweeter, Long> {
    List<Tweeter> findAllByUserId(Long userId);
}
