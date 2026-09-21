package com.threadloop.marketplace.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.threadloop.marketplace.model.ClothesUpload;

public interface ClothesUploadRepository extends MongoRepository<ClothesUpload, String> {

}
