package com.ustreamweb3_backend.controllers;


import com.ustreamweb3_backend.dtos.LeaderboardDTO;
import com.ustreamweb3_backend.entities.LeaderboardEntry;
import com.ustreamweb3_backend.services.LeaderboardService;
import com.ustreamweb3_backend.services.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;




    public LeaderboardController(LeaderboardService leaderboardService, VideoService videoService) {
        this.leaderboardService = leaderboardService;

    }

    // Endpoint to get all leaderboard entries
    @GetMapping("/")
    public ResponseEntity<List<LeaderboardDTO>> getLeaderboard() {
        List<LeaderboardDTO> leaderboard = leaderboardService.getLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }


}
