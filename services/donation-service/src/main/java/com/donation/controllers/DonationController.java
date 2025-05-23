package com.donation.controllers;

import com.donation.dto.UpdateDonationRequest;
import com.donation.model.Donation;
import com.donation.service.DonationService;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/donation")
public class DonationController {

    public final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @GetMapping("/{donationId}")
    public ResponseEntity<Donation> handleGetDonation(@PathVariable("donationId") String donationId) {
        return ResponseEntity.ok(donationService.getDonation(donationId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Donation>> getDonations() {
        return ResponseEntity.ok(donationService.getDonations());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDonation(@RequestParam String donationId) {
        return ResponseEntity.ok(donationService.deleteDonation(donationId));
    }

    @PutMapping("/update/{donationId}")
    public ResponseEntity<Donation> updateDonation(
            @PathVariable String donationId,
            @RequestBody UpdateDonationRequest request
    ) {
        return ResponseEntity.ok(donationService.updateDonation(donationId, request));
    }








}
