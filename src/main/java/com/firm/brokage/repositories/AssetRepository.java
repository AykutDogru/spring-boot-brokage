package com.firm.brokage.repositories;

import com.firm.brokage.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByCustomerId(Long customerId);

    Asset findByCustomerIdAndAssetName(Long customerId, String assetName);

}
