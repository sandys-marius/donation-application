package com.sanrius.controllers;

import com.sanrius.model.Donation;
import com.sanrius.services.DonationService;
import java.util.List;
import org.hibernate.sql.exec.spi.JdbcOperationQueryInsert;
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




}
