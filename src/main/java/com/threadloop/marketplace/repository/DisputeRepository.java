package com.threadloop.marketplace.repository;

import com.threadloop.marketplace.model.Dispute;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisputeRepository extends MongoRepository<Dispute, String> {
    List<Dispute> findAllByOrderByCreatedAtDesc();
}
