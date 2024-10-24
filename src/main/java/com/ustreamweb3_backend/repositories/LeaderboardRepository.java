package com.ustreamweb3_backend.repositories;


import com.ustreamweb3_backend.entities.LeaderboardEntry;
import com.ustreamweb3_backend.entities.User;
import com.ustreamweb3_backend.entities.Videos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Integer> {



    Optional<LeaderboardEntry> findByUserAndVideoUrl(User user, Videos video);

}
