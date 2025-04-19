package com.donation.service;

import com.donation.dto.UpdateDonationRequest;
import com.donation.model.Donation;
import com.donation.repositories.DonationRepository;

import java.time.LocalDateTime;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DonationService {

    private final DonationRepository donationRepository;

    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public Donation getDonation(String donationId) {
        return handleDonationRequest(donationId);
    }

    private Donation handleDonationRequest(String donationId) {
        return donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found with the ID: " + donationId));
    }

    public List<Donation> getDonations() {
        return donationRepository.findAll();
    }

    public String deleteDonation(String donationId) {
        Donation donationToDelete = handleDonationRequest(donationId);
        donationRepository.delete(donationToDelete);
        return "Deleted donation with ID: " + donationId;
    }

    public Donation updateDonation(String donationId, UpdateDonationRequest request) {
        boolean modified = false;

        Donation donation = getDonation(donationId);

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            donation.setTitle(request.getTitle());
            log.info("Modified title: {}", request.getTitle());
            modified = true;
        }

        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            donation.setDescription(request.getDescription());
            log.info("Modified description: {}", request.getDescription());
            modified = true;
        }

        if (modified) {
            log.info("Changes have been made to the donation: {}" , donation);
            donation.setLastModifiedAt(LocalDateTime.now());
        }

        // Save and return the updated donation
        return donationRepository.save(donation);
    }

}
