package com.ustreamweb3_backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="videos")
@Data
public class Videos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "Video_url",nullable = false)
    private String videoUrl;

    @Column(name = "video_name", nullable = false)
    private String videoName;

    @Column(name ="uploadDate", nullable = false)
    private LocalDateTime uploadDate;

    @Column(name ="genre", nullable = false)
    private String genre;



}
