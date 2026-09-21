package com.threadloop.marketplace.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.threadloop.marketplace.dto.ClothesUploadDto;
import com.threadloop.marketplace.model.ClothesUpload;
import com.threadloop.marketplace.model.User;
import com.threadloop.marketplace.repository.ClothesUploadRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UploadService {

     private final ClothesUploadRepository clothesUploadRepository;
     private final Cloudinary cloudinary;

     public UploadService(ClothesUploadRepository clothesUploadRepository, Cloudinary cloudinary) {
          this.clothesUploadRepository = clothesUploadRepository;
          this.cloudinary = cloudinary;
     }

     /**
      * Upload images to Cloudinary and create a clothes listing.
      */
     public ClothesUploadDto uploadClothesListing(
               ClothesUploadDto dto,
               List<MultipartFile> files,
               User owner) throws IOException {

          List<String> imageUrls = new ArrayList<>();
          List<String> publicIds = new ArrayList<>();

          // 1) Upload images to Cloudinary
          for (MultipartFile file : files) {
               if (file.isEmpty()) {
                    continue;
               }
               Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
               String url = (String) result.get("secure_url");
               String publicId = (String) result.get("public_id");
               imageUrls.add(url);
               publicIds.add(publicId);
          }

          // 2) Build entity
          ClothesUpload entity = new ClothesUpload();
          entity.setTitle(dto.getTitle());
          entity.setDescription(dto.getDescription());
          entity.setBrand(dto.getBrand());
          entity.setBrandTier(dto.getBrandTier());
          entity.setCategory(dto.getCategory());
          entity.setSubcategory(dto.getSubcategory());
          entity.setSize(dto.getSize());
          entity.setGender(dto.getGender());
          entity.setCondition(dto.getCondition());
          entity.setConditionNotes(dto.getConditionNotes());
          entity.setMaterial(dto.getMaterial());
          entity.setColor(dto.getColor());
          entity.setOriginalPrice(dto.getOriginalPrice());
          entity.setEstimatedSwapValue(dto.getEstimatedSwapValue());
          entity.setTags(dto.getTags() != null ? dto.getTags() : new ArrayList<>());

          entity.setImages(imageUrls);
          entity.setImageUrl(imageUrls.isEmpty() ? null : imageUrls.get(0));
          entity.setImagePublicId(publicIds.isEmpty() ? null : publicIds.get(0));

          entity.setOwner(owner);
          entity.setStatus("available");
          entity.setEcoSavedKgCo2(0.0);
          entity.setEcoSavedLitersWater(0.0);
          entity.setCreatedAt(Instant.now().toString());

          ClothesUpload saved = clothesUploadRepository.save(entity);

          // 3) Build response DTO
          ClothesUploadDto response = new ClothesUploadDto();
          response.setId(saved.getId());
          response.setTitle(saved.getTitle());
          response.setDescription(saved.getDescription());
          response.setCategory(saved.getCategory());
          response.setSize(saved.getSize());
          response.setBrand(saved.getBrand());
          response.setCondition(saved.getCondition());
          response.setOriginalPrice(saved.getOriginalPrice());
          response.setEstimatedSwapValue(saved.getEstimatedSwapValue());
          response.setImages(saved.getImages());
          response.setImageUrl(saved.getImageUrl());
          response.setImagePublicId(saved.getImagePublicId());
          response.setOwnerId(owner.getId());
          response.setOwnerName(owner.getName());
          response.setStatus(saved.getStatus());
          response.setCreatedAt(saved.getCreatedAt());

          return response;
     }
}