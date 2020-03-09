package com.silverbarsmarketplace.orders.domain;

import java.util.Objects;

public class OrderSummary {

    private final Double price;
    private final Double quantity;

    public OrderSummary(final Double price, final Double quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Double getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderSummary that = (OrderSummary) o;
        return Objects.equals(price, that.price) &&
            Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, quantity);
    }

    @Override
    public String toString() {
        return "OrderSummary{" +
            "price=" + price +
            ", quantity=" + quantity +
            '}';
    }
}
