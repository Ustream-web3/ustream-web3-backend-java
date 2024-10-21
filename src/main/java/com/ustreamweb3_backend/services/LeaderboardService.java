package com.ustreamweb3_backend.services;

import com.ustreamweb3_backend.entities.LeaderboardEntry;

import java.util.List;

public interface LeaderboardService {
    LeaderboardEntry addEntry(LeaderboardEntry entry);
    List<LeaderboardEntry> getAllEntries();
}
