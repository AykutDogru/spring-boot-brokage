package com.firm.brokage.services;

import com.firm.brokage.model.Asset;
import com.firm.brokage.model.Orders;
import com.firm.brokage.repositories.AssetRepository;
import com.firm.brokage.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AssetRepository assetRepository;

    public Orders createOrder(Orders order) {
        // Check if customer has enough assets or TRY
        // Update asset sizes
        return orderRepository.save(order);
    }

    public List<Orders> listOrders(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCustomerIdAndCreateDateBetween(customerId, startDate, endDate);
    }

    public void deleteOrder(Long orderId) {
        // Check if order is PENDING
        // Update asset sizes
        orderRepository.deleteById(orderId);
    }

    public void matchPendingOrders() {
        List<Orders> pendingOrders = orderRepository.findByStatus("PENDING");

        for (Orders order : pendingOrders) {
            Asset tryAsset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), "TRY");
            Asset asset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName());

            if (order.getOrderSide().equals("BUY")) {
                if (tryAsset.getUsableSize() >= order.getSize() * order.getPrice()) {
                    // Update TRY asset
                    tryAsset.setUsableSize((int) (tryAsset.getUsableSize() - order.getSize() * order.getPrice()));
                    asset.setSize(asset.getSize() + order.getSize());
                    asset.setUsableSize(asset.getUsableSize() + order.getSize());
                }
            } else if (order.getOrderSide().equals("SELL")) {
                if (asset.getUsableSize() >= order.getSize()) {
                    // Update asset
                    asset.setUsableSize(asset.getUsableSize() - order.getSize());
                    tryAsset.setSize((int) (tryAsset.getSize() + order.getSize() * order.getPrice()));
                    tryAsset.setUsableSize((int) (tryAsset.getUsableSize() + order.getSize() * order.getPrice()));
                }
            }

            // Update order status to MATCHED
            order.setStatus("MATCHED");
            orderRepository.save(order);
            assetRepository.save(tryAsset);
            assetRepository.save(asset);
        }
    }


}

