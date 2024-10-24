package com.ustreamweb3_backend.repositories;

import com.ustreamweb3_backend.entities.Videos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideosRepository extends JpaRepository<Videos, Integer> {

    List<Videos> findByVideoNameContainingIgnoreCase(String videoName);

    List<Videos>  findByGenre(String genre);
}
