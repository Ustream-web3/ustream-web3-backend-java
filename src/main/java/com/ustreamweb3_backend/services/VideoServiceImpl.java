package com.ustreamweb3_backend.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ustreamweb3_backend.entities.LeaderboardEntry;
import com.ustreamweb3_backend.entities.User;
import com.ustreamweb3_backend.repositories.LeaderboardEntryRepository;
import com.ustreamweb3_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@Service
public class VideoServiceImpl implements VideoService {
    private final Cloudinary cloudinary;
    private final LeaderboardEntryRepository leaderboardEntryRepository;
    private final UserRepository userRepository;


    @Autowired
    public VideoServiceImpl(Cloudinary cloudinary, LeaderboardEntryRepository leaderboardEntryRepository, UserRepository userRepository) {
        this.cloudinary = cloudinary;
        this.leaderboardEntryRepository = leaderboardEntryRepository;
        this.userRepository = userRepository;
    }


    @Override
    public String uploadVideo(MultipartFile file, String adminUsername) throws IOException {
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "video"
        ));
        return uploadResult.get("secure_url").toString();
    }

    @Override
    public String getVideoUrl(String publicId) {
        return cloudinary.url().resourceType("video").generate(publicId);
    }


    @Override
    public void incrementStreamingScore(String username) {
        // Retrieve user based on username
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new RuntimeException("User not found: " + username)
        );

        // Find the leaderboard entry for this user
        LeaderboardEntry entry = leaderboardEntryRepository.findByUser(user);

        if (entry == null) {

            // If no entry exists, create a new one with an initial score of 1
            entry = new LeaderboardEntry();
            entry.setUser(user);
            entry.setScore(1);
            leaderboardEntryRepository.save(entry);
        } else {
            // If entry exists, increment the score
            entry.setScore(entry.getScore() + 1);
            leaderboardEntryRepository.save(entry);
        }
    }




}
