package com.firm.brokage.services;

import com.firm.brokage.model.Asset;
import com.firm.brokage.repositories.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;

    public List<Asset> listAssets(Long customerId) {
        return assetRepository.findByCustomerId(customerId);
    }

    public void depositMoney(Long customerId, int amount) {
        // Fetch the TRY asset for the customer
        Asset tryAsset = assetRepository.findByCustomerIdAndAssetName(customerId, "TRY");

        if (tryAsset != null) {
            // Update the usable size of the TRY asset
            tryAsset.setUsableSize(tryAsset.getUsableSize() + amount);

            // Save the updated asset back to the repository
            assetRepository.save(tryAsset);
        } else {
            // Handle the case where the TRY asset does not exist for the customer
            // You might want to create a new TRY asset for the customer
            Asset newTryAsset = new Asset();
            newTryAsset.setCustomerId(customerId);
            newTryAsset.setAssetName("TRY");
            newTryAsset.setSize(amount);
            newTryAsset.setUsableSize(amount);

            assetRepository.save(newTryAsset);
        }
    }

    public void withdrawMoney(Long customerId, int amount, String iban) {
        // Fetch the TRY asset for the customer
        Asset tryAsset = assetRepository.findByCustomerIdAndAssetName(customerId, "TRY");

        if (tryAsset != null && tryAsset.getUsableSize() >= amount) {
            // Update the usable size of the TRY asset
            tryAsset.setUsableSize(tryAsset.getUsableSize() - amount);

            // Save the updated asset back to the repository
            assetRepository.save(tryAsset);
        } else {
            // Handle the case where there are insufficient funds
            throw new IllegalArgumentException("Insufficient funds");
        }
    }
}
