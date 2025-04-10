package com.sanrius.utils;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Donation {

    private Long donationId;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
