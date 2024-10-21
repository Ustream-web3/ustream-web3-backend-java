package com.ustreamweb3_backend.services;

import com.ustreamweb3_backend.entities.LeaderboardEntry;
import com.ustreamweb3_backend.repositories.LeaderboardEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardServiceImpl implements LeaderboardService{

    private final LeaderboardEntryRepository leaderboardEntryRepository;

    @Autowired
    public LeaderboardServiceImpl(LeaderboardEntryRepository leaderboardEntryRepository) {
        this.leaderboardEntryRepository = leaderboardEntryRepository;
    }


    @Override
    public LeaderboardEntry addEntry(LeaderboardEntry entry) {
        return leaderboardEntryRepository.save(entry);
    }

    @Override
    public List<LeaderboardEntry> getAllEntries() {
        return leaderboardEntryRepository.findAll();

    }
}
