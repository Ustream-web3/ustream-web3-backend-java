package com.ustreamweb3_backend.entities;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name ="leaderboard")
@Data

public class LeaderboardEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn (name = "video_id", nullable = false)
    private Videos video;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @Column(name = "score", nullable = false)
    private int score;



}
