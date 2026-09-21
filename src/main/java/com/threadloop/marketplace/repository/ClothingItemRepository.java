package com.threadloop.marketplace.repository;

import com.threadloop.marketplace.model.ClothingItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothingItemRepository extends MongoRepository<ClothingItem, String> {
    @Query("{ 'owner.$id': ?0 }")
    List<ClothingItem> findByOwnerId(String ownerId);

    List<ClothingItem> findByStatus(String status);

    long countByStatus(String status);
}
