package com.threadloop.marketplace.service;

import com.threadloop.marketplace.dto.PlatformKPIsDto;
import com.threadloop.marketplace.model.PlatformKPI;
import com.threadloop.marketplace.model.User;
import com.threadloop.marketplace.repository.ClothingItemRepository;
import com.threadloop.marketplace.repository.PlatformKPIRepository;
import com.threadloop.marketplace.repository.SwapProposalRepository;
import com.threadloop.marketplace.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KpiService {

    private final PlatformKPIRepository platformKPIRepository;
    private final UserRepository userRepository;
    private final ClothingItemRepository clothingItemRepository;
    private final SwapProposalRepository swapProposalRepository;

    public KpiService(PlatformKPIRepository platformKPIRepository,
                      UserRepository userRepository,
                      ClothingItemRepository clothingItemRepository,
                      SwapProposalRepository swapProposalRepository) {
        this.platformKPIRepository = platformKPIRepository;
        this.userRepository = userRepository;
        this.clothingItemRepository = clothingItemRepository;
        this.swapProposalRepository = swapProposalRepository;
    }

    public PlatformKPIsDto getKPIs() {
        PlatformKPI defaultKpi = platformKPIRepository.findById("global")
                .orElse(new PlatformKPI("global", 14820, 4390, 8740, 12450.0, 23600000.0, 48070.0, 94.8));

        long userCount = userRepository.count();
        long availableItems = clothingItemRepository.countByStatus("available");
        long completedSwaps = swapProposalRepository.countByStatus("completed");

        List<User> users = userRepository.findAll();
        double sumWaste = users.stream().mapToDouble(u -> u.getWasteDivertedKg() != null ? u.getWasteDivertedKg() : 0.0).sum();
        double sumWater = users.stream().mapToDouble(u -> u.getWaterSavedLiters() != null ? u.getWaterSavedLiters() : 0.0).sum();
        double sumCo2 = users.stream().mapToDouble(u -> u.getCo2SavedKg() != null ? u.getCo2SavedKg() : 0.0).sum();

        double totalWaste = defaultKpi.getTotalKgWasteDiverted() + sumWaste;
        double totalWater = defaultKpi.getTotalLitersWaterSaved() + sumWater;
        double totalCo2 = defaultKpi.getTotalKgCo2Avoided() + sumCo2;

        return new PlatformKPIsDto(
                defaultKpi.getTotalUsers() + (int) userCount,
                (int) availableItems,
                defaultKpi.getCompletedSwaps() + (int) completedSwaps,
                Math.round(totalWaste * 10.0) / 10.0,
                (double) Math.round(totalWater),
                Math.round(totalCo2 * 10.0) / 10.0,
                defaultKpi.getSwapSuccessRate()
        );
    }
}
