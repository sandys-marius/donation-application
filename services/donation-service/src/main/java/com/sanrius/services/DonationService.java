package com.sanrius.services;

import com.sanrius.model.Donation;
import com.sanrius.repositories.DonationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

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
}
