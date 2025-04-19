package com.donation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDonationRequest {
    private String title;
    private String description;
}
