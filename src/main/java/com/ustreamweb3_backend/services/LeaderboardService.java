package com.ustreamweb3_backend.services;

import com.ustreamweb3_backend.dtos.LeaderboardDTO;
import com.ustreamweb3_backend.entities.LeaderboardEntry;

import java.util.List;

public interface LeaderboardService {
    List<LeaderboardDTO> getLeaderboard();

}
