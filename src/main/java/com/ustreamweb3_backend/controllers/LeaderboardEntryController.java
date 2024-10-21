package com.ustreamweb3_backend.controllers;


import com.ustreamweb3_backend.entities.LeaderboardEntry;
import com.ustreamweb3_backend.services.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/leaderboard")
public class LeaderboardEntryController {

    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardEntryController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }


    @PostMapping("/add")
    public ResponseEntity<LeaderboardEntry> addEntry(@RequestBody LeaderboardEntry entry) {
        LeaderboardEntry savedEntry = leaderboardService.addEntry(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntry); // Return the created entry
    }

    @GetMapping("/")
    public ResponseEntity<List<LeaderboardEntry>> getEntries() {
        List<LeaderboardEntry> entries = leaderboardService.getAllEntries();
        return ResponseEntity.ok(entries); // Return all leaderboard entries
    }


}
