package com.firm.brokage;

import com.firm.brokage.model.Asset;
import com.firm.brokage.model.Orders;
import com.firm.brokage.repositories.AssetRepository;
import com.firm.brokage.repositories.OrderRepository;
import com.firm.brokage.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        Orders order = new Orders();
        when(orderRepository.save(order)).thenReturn(order);

        Orders createdOrder = orderService.createOrder(order);

        assertEquals(order, createdOrder);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testListOrders() {
        Long customerId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        List<Orders> orders = Arrays.asList(new Orders(), new Orders());
        when(orderRepository.findByCustomerIdAndCreateDateBetween(customerId, startDate, endDate)).thenReturn(orders);

        List<Orders> result = orderService.listOrders(customerId, startDate, endDate);

        assertEquals(orders, result);
        verify(orderRepository, times(1)).findByCustomerIdAndCreateDateBetween(customerId, startDate, endDate);
    }

    @Test
    void testDeleteOrder() {
        Long orderId = 1L;

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void testMatchPendingOrders() {
        Orders order = new Orders();
        order.setOrderSide("BUY");
        order.setSize(10);
        order.setPrice(100);
        order.setCustomerId(1L);
        order.setAssetName("Asset1");
        order.setStatus("PENDING");

        Asset tryAsset = new Asset();
        tryAsset.setCustomerId(1L);
        tryAsset.setAssetName("TRY");
        tryAsset.setUsableSize(1000);

        Asset asset = new Asset();
        asset.setCustomerId(1L);
        asset.setAssetName("Asset1");
        asset.setUsableSize(0);
        asset.setSize(0);

        when(orderRepository.findByStatus("PENDING")).thenReturn(Arrays.asList(order));
        when(assetRepository.findByCustomerIdAndAssetName(1L, "TRY")).thenReturn(tryAsset);
        when(assetRepository.findByCustomerIdAndAssetName(1L, "Asset1")).thenReturn(asset);

        orderService.matchPendingOrders();

        assertEquals("MATCHED", order.getStatus());

        verify(orderRepository, times(1)).save(order);
        verify(assetRepository, times(1)).save(tryAsset);
        verify(assetRepository, times(1)).save(asset);
    }
}
