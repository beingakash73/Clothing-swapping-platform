package com.threadloop.marketplace.repository;

import com.threadloop.marketplace.model.PlatformKPI;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformKPIRepository extends MongoRepository<PlatformKPI, String> {
}
