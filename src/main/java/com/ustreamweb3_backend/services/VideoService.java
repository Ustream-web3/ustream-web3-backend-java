package com.ustreamweb3_backend.services;


import com.ustreamweb3_backend.entities.User;
import com.ustreamweb3_backend.entities.Videos;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VideoService {

    List<Videos> getVideosByName (String videoName);

    List<Videos> getVideosByGenre( String genre);

    String uploadVideo(MultipartFile file, String videoName, String genre) throws Exception;
    // Admin only
    String streamVideo(int videoId);

    void incrementStreamingScore (User user, Videos video);

}
