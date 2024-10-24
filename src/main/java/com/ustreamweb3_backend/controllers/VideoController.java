package com.ustreamweb3_backend.controllers;


import com.ustreamweb3_backend.entities.Videos;
import com.ustreamweb3_backend.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;


    // Endpoint for to search for videos by name
    @GetMapping("/search")
    public ResponseEntity<List<Videos>> getVideosByName(@RequestParam("videoName") String videoName ) {
        List<Videos> videos = videoService.getVideosByName(videoName);
        return ResponseEntity.ok(videos);
    }

    // Endpoints to get all videos by genre
    @GetMapping("/filter")
    public ResponseEntity<List<Videos>> getVideosByGenre(@RequestParam("genre") String genre) {
        List<Videos> videos = videoService.getVideosByGenre(genre);
        return ResponseEntity.ok(videos);
    }

    // Endopoint for uploading a videos
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file,
                                              @RequestParam("videoName") String videoName,
                                              @RequestParam("genre") String genre) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Current user roles: " + authentication.getAuthorities());
        String videoUrl = videoService.uploadVideo(file, videoName, genre);
        return ResponseEntity.ok("Video successfully upload: " + videoUrl);
    }

    // Endpoint to stream a video
    @GetMapping("/stream/{videoId}")
    public ResponseEntity<String> streamVideo(@PathVariable int videoId) {
        try {
            // Get the currently authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // This retrieves

            String videoUrl = videoService.streamVideo(videoId);
            return ResponseEntity.ok(videoUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Video not found: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body("User not authenticated: " + e.getMessage());
        }
    }


}

