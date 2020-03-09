package com.silverbarsmarketplace.orders.validator;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.silverbarsmarketplace.orders.domain.Order;

/**
 * For validation I choose some random validation rules just to make a point.
 * In real life business representatives would dictate the validation rules.
 */
public class OrderValidatorImpl implements OrderValidator {

    @Override
    public void validate(final Order order) {
        if (StringUtils.isBlank(order.getUserId())) {
            throw new IllegalArgumentException("Bad user id.");
        }

        if (Objects.isNull(order.getQuantity()) || order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Bad quantity.");
        }

        if (Objects.isNull(order.getPrice()) || order.getPrice() <= 0) {
            throw new IllegalArgumentException("Bad price.");
        }
    }
}
