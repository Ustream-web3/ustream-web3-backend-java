package com.ustreamweb3_backend.services;

import com.ustreamweb3_backend.dtos.LeaderboardDTO;
import com.ustreamweb3_backend.entities.LeaderboardEntry;
import com.ustreamweb3_backend.repositories.LeaderboardRepository;
import com.ustreamweb3_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {

    private final LeaderboardRepository leaderboardEntryRepository;
    private final UserRepository userRepository; // Added userRepository for future use if needed

    @Autowired
    public LeaderboardServiceImpl(LeaderboardRepository leaderboardEntryRepository, UserRepository userRepository) {
        this.leaderboardEntryRepository = leaderboardEntryRepository;
        this.userRepository = userRepository; // Initialize userRepository
    }

    @Override
    public List<LeaderboardDTO> getLeaderboard() { // Change return type to List<LeaderboardDTO>
        List<LeaderboardEntry> entries = leaderboardEntryRepository.findAll();

        return entries.stream()
                .map(entry -> LeaderboardDTO.builder() // Use Builder pattern from Lombok
                        .fullName(entry.getUser().getFullName())
                        .score(entry.getScore()) // Use the correct field name
                        .build())
                .collect(Collectors.toList());
    }
}
