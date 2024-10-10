package com.firm.brokage.controller;

import com.firm.brokage.model.Asset;
import com.firm.brokage.services.AssetService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {
    @Autowired
    private AssetService assetService;

    @GetMapping
    @SecurityRequirement(name = "BasicAuth")
    public ResponseEntity<List<Asset>> listAssets(@RequestParam Long customerId) {
        return ResponseEntity.ok(assetService.listAssets(customerId));
    }

    @PostMapping("/deposit")
    @SecurityRequirement(name = "BasicAuth")
    public ResponseEntity<Void> depositMoney(@RequestParam Long customerId, @RequestParam int amount) {
        assetService.depositMoney(customerId, amount);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/withdraw")
    @SecurityRequirement(name = "BasicAuth")
    public ResponseEntity<Void> withdrawMoney(@RequestParam Long customerId, @RequestParam int amount, @RequestParam String iban) {
        assetService.withdrawMoney(customerId, amount, iban);
        return ResponseEntity.noContent().build();
    }
}
