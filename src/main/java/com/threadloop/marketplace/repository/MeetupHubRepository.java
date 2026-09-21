package com.threadloop.marketplace.repository;

import com.threadloop.marketplace.model.MeetupHub;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetupHubRepository extends MongoRepository<MeetupHub, String> {
}
