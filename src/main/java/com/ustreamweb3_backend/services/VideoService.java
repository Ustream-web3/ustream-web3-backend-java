package com.ustreamweb3_backend.services;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoService {

    String uploadVideo(MultipartFile file, String adminUsername) throws IOException; // Admin only
    String getVideoUrl(String publicId);
    void incrementStreamingScore (String username);

}
