package com.firm.brokage;

import com.firm.brokage.model.Asset;
import com.firm.brokage.repositories.AssetRepository;
import com.firm.brokage.services.AssetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetService assetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListAssets() {
        Long customerId = 1L;
        List<Asset> assets = Arrays.asList(new Asset(), new Asset());
        when(assetRepository.findByCustomerId(customerId)).thenReturn(assets);

        List<Asset> result = assetService.listAssets(customerId);

        assertEquals(assets, result);
        verify(assetRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    void testDepositMoney_existingAsset() {
        Long customerId = 1L;
        int amount = 100;
        Asset tryAsset = new Asset();
        tryAsset.setCustomerId(customerId);
        tryAsset.setAssetName("TRY");
        tryAsset.setUsableSize(200);

        when(assetRepository.findByCustomerIdAndAssetName(customerId, "TRY")).thenReturn(tryAsset);

        assetService.depositMoney(customerId, amount);

        assertEquals(300, tryAsset.getUsableSize());
        verify(assetRepository, times(1)).save(tryAsset);
    }

    @Test
    void testDepositMoney_newAsset() {
        Long customerId = 1L;
        int amount = 100;

        when(assetRepository.findByCustomerIdAndAssetName(customerId, "TRY")).thenReturn(null);

        assetService.depositMoney(customerId, amount);

        verify(assetRepository, times(1)).save(any(Asset.class));
    }

    @Test
    void testWithdrawMoney_sufficientFunds() {
        Long customerId = 1L;
        int amount = 100;
        String iban = "TR1234567890";
        Asset tryAsset = new Asset();
        tryAsset.setCustomerId(customerId);
        tryAsset.setAssetName("TRY");
        tryAsset.setUsableSize(200);

        when(assetRepository.findByCustomerIdAndAssetName(customerId, "TRY")).thenReturn(tryAsset);

        assetService.withdrawMoney(customerId, amount, iban);

        assertEquals(100, tryAsset.getUsableSize());
        verify(assetRepository, times(1)).save(tryAsset);
    }

    @Test
    void testWithdrawMoney_insufficientFunds() {
        Long customerId = 1L;
        int amount = 300;
        String iban = "TR1234567890";
        Asset tryAsset = new Asset();
        tryAsset.setCustomerId(customerId);
        tryAsset.setAssetName("TRY");
        tryAsset.setUsableSize(200);

        when(assetRepository.findByCustomerIdAndAssetName(customerId, "TRY")).thenReturn(tryAsset);

        assertThrows(IllegalArgumentException.class, () -> assetService.withdrawMoney(customerId, amount, iban));
    }
}

