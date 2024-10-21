package com.ustreamweb3_backend.repositories;


import com.ustreamweb3_backend.entities.LeaderboardEntry;
import com.ustreamweb3_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface LeaderboardEntryRepository extends JpaRepository<LeaderboardEntry, Integer> {

    LeaderboardEntry findByUser (User user);

    LeaderboardEntry findByVideoUrl (String videoUrl);
}
