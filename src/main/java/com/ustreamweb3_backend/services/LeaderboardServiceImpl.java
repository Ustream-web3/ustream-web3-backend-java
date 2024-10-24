package com.ustreamweb3_backend.services;

import com.ustreamweb3_backend.entities.LeaderboardEntry;
import com.ustreamweb3_backend.repositories.LeaderboardRepository;
import com.ustreamweb3_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LeaderboardServiceImpl implements LeaderboardService{

    private final LeaderboardRepository leaderboardEntryRepository;
    private final UserRepository userRepository;

    @Autowired
    public LeaderboardServiceImpl(LeaderboardRepository leaderboardEntryRepository, UserRepository userRepository) {
        this.leaderboardEntryRepository = leaderboardEntryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<LeaderboardEntry> getLeaderboard() {
        return leaderboardEntryRepository.findAll(); // Fetch all entries from the repository
    }

}
