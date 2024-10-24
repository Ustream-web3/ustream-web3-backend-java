package com.ustreamweb3_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor // This will generate a constructor with parameters for all fields
public class LeaderboardDTO {

    private String fullName;
    private int score; // Use lowercase 's' for consistency
}
