package com.ustreamweb3_backend.controllers;


import com.ustreamweb3_backend.entities.LeaderboardEntry;
import com.ustreamweb3_backend.services.LeaderboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;


    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    // Endpoint to get all leaderboard entries
    @GetMapping("/")
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard() {
        List<LeaderboardEntry> leaderboardEntries =leaderboardService.getLeaderboard();
        return ResponseEntity.ok(leaderboardEntries);

    }


}
