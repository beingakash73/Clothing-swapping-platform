package com.threadloop.marketplace.service;

import com.threadloop.marketplace.dto.MeetupLocationDto;
import com.threadloop.marketplace.model.MeetupHub;
import com.threadloop.marketplace.repository.MeetupHubRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HubService {

    private final MeetupHubRepository meetupHubRepository;

    public HubService(MeetupHubRepository meetupHubRepository) {
        this.meetupHubRepository = meetupHubRepository;
    }

    public List<MeetupLocationDto> getAllHubs() {
        List<MeetupHub> hubs = meetupHubRepository.findAll();
        return hubs.stream().map(h -> new MeetupLocationDto(
                h.getName(),
                h.getAddress(),
                h.getType(),
                h.getLat(),
                h.getLng()
        )).collect(Collectors.toList());
    }
}
