package com.ustreamweb3_backend.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ustreamweb3_backend.entities.LeaderboardEntry;
import com.ustreamweb3_backend.entities.User;
import com.ustreamweb3_backend.entities.Videos;
import com.ustreamweb3_backend.repositories.LeaderboardRepository;
import com.ustreamweb3_backend.repositories.UserRepository;
import com.ustreamweb3_backend.repositories.VideosRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideosRepository videosRepository;
    private final Cloudinary cloudinary;
    private final LeaderboardRepository leaderboardEntryRepository;
    private final UserRepository userRepository;

    public VideoServiceImpl(VideosRepository videosRepository, Cloudinary cloudinary,
                            LeaderboardRepository leaderboardEntryRepository, UserRepository userRepository) {
        this.videosRepository = videosRepository;
        this.cloudinary = cloudinary;
        this.leaderboardEntryRepository = leaderboardEntryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Videos> getVideosByName(String videoName) {
        return videosRepository.findByVideoNameContainingIgnoreCase(videoName);
    }

    @Override
    public List<Videos> getVideosByGenre(String genre) {
        return videosRepository.findByGenre(genre);
    }

    @Override
    public String uploadVideo(MultipartFile file, String videoName, String genre) throws Exception {

        // Print out the role of the current user
        String userRole = getCurrentUserRole(); // Assuming you have a method to get the role
        System.out.println("Current user role: " + userRole);

        // Check if the current user has ADMIN role
        if(!isUserAdmin()) {
            throw new IllegalStateException("Only admins can upload videos");

        }

        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "video"));
        String videoUrl = uploadResult.get("url").toString();

        Videos video = new Videos();
        video.setVideoUrl(videoUrl);
        video.setVideoName(videoName);
        video.setUploadDate(LocalDateTime.now());
        video.setGenre(genre);
        videosRepository.save(video);

        return videoUrl;
    }

    @Override
    public String streamVideo(int videoId) {
        Videos video = videosRepository.findById(videoId).orElseThrow(() ->
                new IllegalArgumentException("Video not found")
        );

        // Get the currently logged-in user
        User user = getCurrentLoggedInUser();
        if (user == null) {
            throw new IllegalStateException("User not authenticated");
        }

        // Increment the score on the leaderboard
        incrementStreamingScore(user, video);
        return video.getVideoUrl();
    }

    @Override
    public void incrementStreamingScore(User user, Videos video) {
        // Find or create a new leaderboard entry
        LeaderboardEntry leaderboardEntry = leaderboardEntryRepository
                .findByUserAndVideoUrl(user, video)
                .orElse(new LeaderboardEntry());

        // Set the user and video
        leaderboardEntry.setUser(user);
        leaderboardEntry.setVideo(video);
        leaderboardEntry.setScore(leaderboardEntry.getScore() + 1); // Increment score

        // Save the updated leaderboard entry
        leaderboardEntryRepository.save(leaderboardEntry);
    }


    private User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String username = null;

            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                username = (String) principal;
            }

            if (username != null) {
                return userRepository.findByUsername(username).orElse(null);
            }
        }
        return null;
    }

    private boolean isUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst().orElse("UNKNOWN");
        }
        return "UNKNOWN";
    }

}


