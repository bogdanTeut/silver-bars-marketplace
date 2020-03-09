package com.silverbarsmarketplace.orders.domain;

import java.util.Objects;

public class Order {

    private final String userId;
    private final Double quantity;
    private final Double price;
    private final OrderType orderType;

    public Order(String userId, Double quantity, Double price, OrderType orderType) {
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.orderType = orderType;
    }

    public String getUserId() {
        return userId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(userId, order.userId) &&
            Objects.equals(quantity, order.quantity) &&
            Objects.equals(price, order.price) &&
            orderType == order.orderType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, quantity, price, orderType);
    }

    @Override
    public String toString() {
        return "Order{" +
            "userId='" + userId + '\'' +
            ", quantity=" + quantity +
            ", price=" + price +
            ", orderType=" + orderType +
            '}';
    }
}
