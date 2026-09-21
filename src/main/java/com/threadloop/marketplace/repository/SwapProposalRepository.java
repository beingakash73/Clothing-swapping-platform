package com.threadloop.marketplace.repository;

import com.threadloop.marketplace.model.SwapProposal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwapProposalRepository extends MongoRepository<SwapProposal, String> {

    List<SwapProposal> findAllByOrderByUpdatedAtDesc();

    List<SwapProposal> findByStatusOrderByUpdatedAtDesc(String status);

    @Query(value = "{ '$or': [ { 'requester.$id': ?0 }, { 'receiver.$id': ?0 } ] }", sort = "{ 'updatedAt': -1 }")
    List<SwapProposal> findByUserInvolved(String userId);

    @Query(value = "{ '$and': [ { '$or': [ { 'requester.$id': ?0 }, { 'receiver.$id': ?0 } ] }, { 'status': ?1 } ] }", sort = "{ 'updatedAt': -1 }")
    List<SwapProposal> findByUserInvolvedAndStatus(String userId, String status);

    long countByStatus(String status);
}
