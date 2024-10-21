package com.ustreamweb3_backend.controllers;


import com.ustreamweb3_backend.services.VideoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/videos")
public class VideoController {

    private final VideoServiceImpl videoService;

    public VideoController(VideoServiceImpl videoService) {
        this.videoService = videoService;
    }

    // Endpoint to upload video - restricted to admins only
    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file, Authentication authentication) {
        try {
            String adminUsername = authentication.getName(); // Retrieve logged-in admin username
            String videoUrl = videoService.uploadVideo(file, adminUsername);
            return ResponseEntity.ok(videoUrl);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload video");
        }
    }

    // Endpoint to get video stream URL by public ID
    @GetMapping("/stream/{publicId}")
    public ResponseEntity<String> streamVideo(@PathVariable String publicId, Authentication authentication) {
        String videoUrl = videoService.getVideoUrl(publicId);
        String userId = authentication.getName();
        videoService.incrementStreamingScore(userId);
        return ResponseEntity.ok(videoUrl);
    }
}
