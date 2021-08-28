package io.trzcinski.test.store;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;

import io.trzcinski.test.commons.dto.Order;

interface StoreClient {

    @GetMapping("/store/inventory")
    Map<String, Object> getInventory(
    );

    @PostMapping("/store/order")
    Order placeOrder(
        @RequestBody() Order payload
    );

    @GetMapping("/store/order/{orderId}")
    Order getOrderById(
        @RequestParam("orderId") Integer orderId
    );

    @DeleteMapping("/store/order/{orderId}")
    void deleteOrder(
        @RequestParam("orderId") Integer orderId
    );
}