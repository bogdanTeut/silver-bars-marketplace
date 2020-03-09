package com.silverbarsmarketplace.orders;

import java.util.List;

import com.silverbarsmarketplace.orders.domain.Order;
import com.silverbarsmarketplace.orders.domain.OrderSummary;
import com.silverbarsmarketplace.orders.domain.OrderType;

public interface LiveOrderBoard {

    void register(Order order);

    void cancel(Order order);

    List<OrderSummary> getOrderSummary(OrderType orderType);
}
