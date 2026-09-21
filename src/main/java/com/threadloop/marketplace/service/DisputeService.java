package com.threadloop.marketplace.service;

import com.threadloop.marketplace.dto.CreateDisputeRequest;
import com.threadloop.marketplace.dto.DisputeDto;
import com.threadloop.marketplace.dto.ResolveDisputeRequest;
import com.threadloop.marketplace.model.Dispute;
import com.threadloop.marketplace.model.User;
import com.threadloop.marketplace.repository.DisputeRepository;
import com.threadloop.marketplace.repository.SwapProposalRepository;
import com.threadloop.marketplace.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisputeService {

    private final DisputeRepository disputeRepository;
    private final UserRepository userRepository;
    private final SwapProposalRepository swapProposalRepository;
    private final UserService userService;
    private final SwapService swapService;

    public DisputeService(DisputeRepository disputeRepository,
                          UserRepository userRepository,
                          SwapProposalRepository swapProposalRepository,
                          UserService userService,
                          SwapService swapService) {
        this.disputeRepository = disputeRepository;
        this.userRepository = userRepository;
        this.swapProposalRepository = swapProposalRepository;
        this.userService = userService;
        this.swapService = swapService;
    }

    public List<DisputeDto> getAllDisputes() {
        return disputeRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DisputeDto createDispute(CreateDisputeRequest request) {
        User reporter = userRepository.findById(request.getReporterId())
                .orElseThrow(() -> new RuntimeException("Reporter not found: " + request.getReporterId()));

        User reportedUser = userRepository.findById(request.getReportedUserId())
                .orElseThrow(() -> new RuntimeException("Reported user not found: " + request.getReportedUserId()));

        String disputeId = "disp_" + System.currentTimeMillis();
        String now = Instant.now().toString();

        Dispute dispute = new Dispute(
                disputeId,
                request.getSwapId(),
                reporter,
                reportedUser,
                request.getReason() != null ? request.getReason() : "other",
                request.getDescription() != null ? request.getDescription() : "",
                "open",
                null,
                now
        );

        Dispute saved = disputeRepository.save(dispute);
        return toDto(saved);
    }

    @Transactional
    public Optional<DisputeDto> resolveDispute(String id, ResolveDisputeRequest request) {
        return disputeRepository.findById(id).map(dispute -> {
            dispute.setStatus(request.getStatus() != null ? request.getStatus() : "resolved");
            if (request.getResolutionNotes() != null) {
                dispute.setResolutionNotes(request.getResolutionNotes());
            }
            Dispute updated = disputeRepository.save(dispute);
            return toDto(updated);
        });
    }

    public DisputeDto toDto(Dispute d) {
        DisputeDto dto = new DisputeDto();
        dto.setId(d.getId());
        dto.setSwapId(d.getSwapId());
        dto.setReporterId(d.getReporter() != null ? d.getReporter().getId() : null);
        dto.setReportedUserId(d.getReportedUser() != null ? d.getReportedUser().getId() : null);
        dto.setReason(d.getReason());
        dto.setDescription(d.getDescription());
        dto.setStatus(d.getStatus());
        dto.setResolutionNotes(d.getResolutionNotes());
        dto.setCreatedAt(d.getCreatedAt());

        if (d.getReporter() != null) dto.setReporter(userService.toDto(d.getReporter()));
        if (d.getReportedUser() != null) dto.setReportedUser(userService.toDto(d.getReportedUser()));
        if (d.getSwapId() != null) {
            swapProposalRepository.findById(d.getSwapId()).ifPresent(s -> dto.setSwap(swapService.toDto(s)));
        }

        return dto;
    }
}
