package com.threadloop.marketplace.repository;

import com.threadloop.marketplace.model.ClothesUpload;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ClothesUploadRepository extends MongoRepository<ClothesUpload, String> {
    List<ClothesUpload> findByCategory(String category);
    List<ClothesUpload> findByStatus(String status);
}
