package com.silverbarsmarketplace.orders.repository;

import java.util.List;

import com.silverbarsmarketplace.orders.domain.Order;

/**
 * This repository can be anything, a DB call, a RestHttp call. It will just be mocked.
 */
public interface OrderRepository {

    void saveOrder(Order order);

    void delete(Order order);

    List<Order> getOrders();
}
