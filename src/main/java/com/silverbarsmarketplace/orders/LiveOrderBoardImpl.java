package com.silverbarsmarketplace.orders;

import static com.silverbarsmarketplace.orders.domain.OrderType.SELL;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.silverbarsmarketplace.orders.domain.Order;
import com.silverbarsmarketplace.orders.domain.OrderSummary;
import com.silverbarsmarketplace.orders.domain.OrderType;
import com.silverbarsmarketplace.orders.repository.OrderRepository;
import com.silverbarsmarketplace.orders.validator.OrderValidator;

public class LiveOrderBoardImpl implements LiveOrderBoard {

    private static final double ZERO = 0.0;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public LiveOrderBoardImpl(final OrderRepository orderRepository, final OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Override
    public void register(final Order order) {
        Objects.requireNonNull(order, "Order is null.");
        this.orderValidator.validate(order);

        this.orderRepository.saveOrder(order);
    }

    @Override
    public void cancel(final Order order) {
        this.orderRepository.delete(order);
    }

    @Override
    public List<OrderSummary> getOrderSummary(final OrderType orderType) {
        List<Order> orderList = this.orderRepository.getOrders();
        Map<OrderType, Map<Double, OrderSummary>> ordersGroupedByType = orderList
            .stream()
            //group by order type
            .collect(groupingBy(Order::getOrderType,
                                //group by price
                                groupingBy(Order::getPrice,
                                            //map to OrderSummary
                                            mapping(orderOrderSummaryFunction(),
                                                    //reduce by summing quantities
                                                    reducing(identityOrderSummary(),
                                                            reduceOrderSummary())))
            ));

        Comparator<OrderSummary> comparator = fetchComparator(orderType);

        if (!ordersGroupedByType.containsKey(orderType)) {
            return Collections.emptyList();
        }

        return ordersGroupedByType.get(orderType)
                .values()
                .stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private Function<Order, OrderSummary> orderOrderSummaryFunction() {
        return order -> new OrderSummary(order.getPrice(), order.getQuantity());
    }

    private OrderSummary identityOrderSummary() {
        return new OrderSummary(ZERO, ZERO);
    }

    private BinaryOperator<OrderSummary> reduceOrderSummary() {
        return (orderSummary1, orderSummary2) -> new OrderSummary(orderSummary2.getPrice(), orderSummary1.getQuantity() + orderSummary2.getQuantity());
    }

    private Comparator<OrderSummary> fetchComparator(OrderType orderType) {
        return orderType == SELL ?
            Comparator.comparing(OrderSummary::getPrice) :
            Comparator.comparing(OrderSummary::getPrice).reversed();
    }
}
