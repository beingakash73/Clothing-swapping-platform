package com.threadloop.marketplace.repository;

import com.threadloop.marketplace.model.Badge;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeRepository extends MongoRepository<Badge, String> {
    @Query("{ 'user.$id': ?0 }")
    List<Badge> findByUserId(String userId);
}
