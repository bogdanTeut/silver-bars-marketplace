package com.silverbarsmarketplace.orders.validator;

import com.silverbarsmarketplace.orders.domain.Order;

public interface OrderValidator {

    void validate(Order order);
}
