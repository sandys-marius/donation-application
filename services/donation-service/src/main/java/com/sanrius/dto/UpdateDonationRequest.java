package com.sanrius.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDonationRequest {
    private String title;
    private String description;
}
