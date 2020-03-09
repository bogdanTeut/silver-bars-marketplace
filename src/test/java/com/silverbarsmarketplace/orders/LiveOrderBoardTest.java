package com.silverbarsmarketplace.orders;

import static com.silverbarsmarketplace.orders.domain.OrderType.BUY;
import static com.silverbarsmarketplace.orders.domain.OrderType.SELL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.silverbarsmarketplace.orders.domain.Order;
import com.silverbarsmarketplace.orders.domain.OrderSummary;
import com.silverbarsmarketplace.orders.domain.OrderType;
import com.silverbarsmarketplace.orders.exception.OrderNorFoundExceptionFound;
import com.silverbarsmarketplace.orders.repository.OrderRepository;
import com.silverbarsmarketplace.orders.validator.OrderValidator;
import com.silverbarsmarketplace.orders.validator.OrderValidatorImpl;

public class LiveOrderBoardTest {

    @Mock
    private OrderRepository orderRepository;

    private LiveOrderBoard liveOrderBoard;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        OrderValidator orderValidator = new OrderValidatorImpl();
        this.liveOrderBoard = new LiveOrderBoardImpl(orderRepository, orderValidator);
    }

    @AfterMethod
    public void tearDown() {
        Mockito.reset(this.orderRepository);
    }


    @Test
    public void testRegisterOrder() {

        //given
        Order order = new Order("user1", 3.5, 303.0, SELL);

        //when
        this.liveOrderBoard.register(order);

        //then
        Mockito.verify(this.orderRepository).saveOrder(order);
    }

    @Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "Order is null.")
    public void givenNullOrder_whenRegistering_itShouldThrowIllegalArgumentException() {
        //when
        this.liveOrderBoard.register(null);
    }

//    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Bad user id.")
//    public void givenNullUserId_whenRegisteringUser_thenItShouldThrowException() {
//        //given
//        Order order = new Order(null, 3.5, 303.0, SELL);
//
//        //when
//        this.liveOrderBoard.register(order);
//    }
//
//    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Bad user id.")
//    public void givenBadUserId_whenRegisteringUser_thenItShouldThrowException() {
//        //given
//        Order order = new Order("", 3.5, 303.0, SELL);
//
//        //when
//        this.liveOrderBoard.register(order);
//    }
//
//    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Bad quantity.")
//    public void givenNullQuantity_whenRegisteringUser_thenItShouldThrowException() {
//        //given
//        Order order = new Order("userId", null, 303.0, SELL);
//
//        //when
//        this.liveOrderBoard.register(order);
//    }
//
//    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Bad quantity.")
//    public void givenZeroQuantity_whenRegisteringUser_thenItShouldThrowException() {
//        //given
//        Order order = new Order("userId", 0.0, 303.0, SELL);
//
//        //when
//        this.liveOrderBoard.register(order);
//    }
//
//    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Bad quantity.")
//    public void givenNegativeQuantity_whenRegisteringUser_thenItShouldThrowException() {
//        //given
//        Order order = new Order("userId", -10.0, 303.0, SELL);
//
//        //when
//        this.liveOrderBoard.register(order);
//    }
//
//    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Bad price.")
//    public void givenNullPrice_whenRegisteringUser_thenItShouldThrowException() {
//        //given
//        Order order = new Order("userId", 3.5, null, SELL);
//
//        //when
//        this.liveOrderBoard.register(order);
//    }
//
//    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Bad price.")
//    public void givenZeroPrice_whenRegisteringUser_thenItShouldThrowException() {
//        //given
//        Order order = new Order("userId", 3.5, 0.0, SELL);
//
//        //when
//        this.liveOrderBoard.register(order);
//    }
//
//    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Bad price.")
//    public void givenNegativePrice_whenRegisteringUser_thenItShouldThrowException() {
//        //given
//        Order order = new Order("userId", 3.5, -10.0, SELL);
//
//        //when
//        this.liveOrderBoard.register(order);
//    }
//
    @Test(dataProvider = "negativeValidationTestCases")
    public void givenTheNegativeTestCase_whenRegisteringOrder_thenItShouldThrowException(
        String userId,
        Double quantity,
        Double price,
        OrderType orderType,
        String expectedErrorMessage
    ) {
        //given
        Order order = new Order(userId, quantity, price, orderType);

        //when
        try {
            liveOrderBoard.register(order);
        } catch (IllegalArgumentException e) {
            //then
            assertThat(e.getMessage(), containsString(expectedErrorMessage));
        }
    }

    @DataProvider(name = "negativeValidationTestCases")
    public Object[][] negativeValidationTestCases() {
        return new Object[][]{
            //user id       //quantity      //price       //order type    //expected message
            {null,          3.5,            303.0,        SELL,            "Bad user id."},
            {"",            3.5,            303.0,        SELL,            "Bad user id."},
            {"user1",       null,           303.0,        SELL,            "Bad quantity."},
            {"user1",       0.0,            303.0,        SELL,            "Bad quantity."},
            {"user1",     -10.0,            303.0,        SELL,            "Bad quantity."},
            {"user1",       3.5,             null,        SELL,            "Bad price."},
            {"user1",       3.5,              0.0,        SELL,            "Bad price."},
            {"user1",       3.5,            -10.0,        SELL,            "Bad price."},
        };
    }

    @Test
    public void testCancelOrder() {
        //given
        Order order = new Order("user1", 3.5, 303.0, SELL);

        //when
        this.liveOrderBoard.cancel(order);

        //then
        Mockito.verify(this.orderRepository).delete(order);
    }

    @Test(expectedExceptions = OrderNorFoundExceptionFound.class)
    public void givenOrderNotFound_whenCancelingOrder_thenOrderNorFoundExceptionFound() {
        //given
        Order order = new Order("user1", 3.5, 303.0, SELL);
        stubOrderNotFound(order);

        //when
        this.liveOrderBoard.cancel(order);
    }

    @Test
    public void testGetOrderSummary() {
        //given
        when(this.orderRepository.getOrders())
            .thenReturn(List.of(new Order("user1", 3.5, 306.0, SELL),
                                new Order("user2", 1.2, 310.0, SELL),
                                new Order("user3", 1.5, 307.0, SELL)
        ));

        List<OrderSummary> expectedOrderSummaryList = List.of(new OrderSummary(306.0, 3.5),
                                                            new OrderSummary(310.0, 1.2),
                                                            new OrderSummary(307.0, 1.5));

        //when
        List<OrderSummary> orderSummaryList = this.liveOrderBoard.getOrderSummary(SELL);

        //then
        MatcherAssert.assertThat(orderSummaryList, containsInAnyOrder(expectedOrderSummaryList.toArray()));
    }

    @Test
    public void givenNoOrders_thenShouldReturnAnEmptyList() {
        //given
        when(this.orderRepository.getOrders()).thenReturn(Collections.emptyList());

        //when
        List<OrderSummary> orderSummaryList = this.liveOrderBoard.getOrderSummary(SELL);

        //then
        MatcherAssert.assertThat(orderSummaryList, containsInAnyOrder(Collections.emptyList()));
    }

    @Test
    public void givenMultipleOrdersWithTheSamePrice_whenGettingOrderSummary_thenQuantitiesShouldBeMerged() {
        //given
        when(this.orderRepository.getOrders())
            .thenReturn(List.of(new Order("user1", 3.5, 306.0, SELL),
                                new Order("user2", 1.2, 310.0, SELL),
                                new Order("user3", 1.5, 307.0, SELL),
                                new Order("user4", 2.0, 306.0, SELL)
        ));

        List<OrderSummary> expectedOrderSummaryList = List.of(new OrderSummary(306.0, 5.5),
                                                            new OrderSummary(310.0, 1.2),
                                                            new OrderSummary(307.0, 1.5));

        //when
        List<OrderSummary> orderSummaryList = this.liveOrderBoard.getOrderSummary(SELL);

        //then
        MatcherAssert.assertThat(orderSummaryList, containsInAnyOrder(expectedOrderSummaryList.toArray()));
    }

    @Test
    public void givenMultipleSELLOrders_whenGettingOrderSummary_thenLowestPricesEntriesAreReturnedFirst() {
        //given
        when(this.orderRepository.getOrders())
            .thenReturn(List.of(new Order("user2", 1.2, 310.0, SELL),
                                new Order("user1", 3.5, 306.0, SELL),
                                new Order("user3", 3.5, 426.0, SELL),
                                new Order("user4", 1.5, 307.0, SELL)
            ));

        List<OrderSummary> expectedOrderSummaryList = List.of(new OrderSummary(306.0, 3.5),
                                                              new OrderSummary(307.0, 1.5),
                                                              new OrderSummary(310.0, 1.2),
                                                              new OrderSummary(426.0, 3.5));

        //when
        List<OrderSummary> orderSummaryList = this.liveOrderBoard.getOrderSummary(SELL);

        //then
        assertThat(orderSummaryList, contains(expectedOrderSummaryList.toArray()));
    }

    @Test
    public void givenMultipleBUYOrders_whenGettingOrderSummary_thenHighestPricesEntriesAreReturnedFirst() {
        //given
        when(this.orderRepository.getOrders())
            .thenReturn(List.of(new Order("user1", 1.2, 310.0, BUY),
                                new Order("user2", 3.5, 306.0, BUY),
                                new Order("user3", 3.5, 426.0, BUY),
                                new Order("user4", 1.5, 307.0, BUY)
            ));

        List<OrderSummary> expectedOrderSummaryList = List.of(new OrderSummary(426.0, 3.5),
                                                              new OrderSummary(310.0, 1.2),
                                                              new OrderSummary(307.0, 1.5),
                                                              new OrderSummary(306.0, 3.5));

        //when
        List<OrderSummary> orderSummaryList = this.liveOrderBoard.getOrderSummary(BUY);

        //then
        assertThat(orderSummaryList, contains(expectedOrderSummaryList.toArray()));
    }

    @Test
    public void testGettingOrderSummaryForMultipleBUYAndSELLOrdersWithTheSamePrice() {
        //given
        when(this.orderRepository.getOrders())
            .thenReturn(List.of(new Order("user1", 3.5, 306.0, BUY),
                                new Order("user2", 1.2, 310.0, BUY),
                                new Order("user3", 1.5, 307.0, BUY),
                                new Order("user4", 2.0, 306.0, BUY),
                                new Order("user1", 3.5, 306.0, SELL),
                                new Order("user2", 1.2, 310.0, SELL),
                                new Order("user3", 1.5, 307.0, SELL),
                                new Order("user4", 2.0, 306.0, SELL)
            ));

        List<OrderSummary> expectedSellOrderSummaryList = List.of(new OrderSummary(306.0, 5.5),
                                                                  new OrderSummary(307.0, 1.5),
                                                                  new OrderSummary(310.0, 1.2));

        List<OrderSummary> expectedBuyOrdersSummaryList = List.of(new OrderSummary(310.0, 1.2),
                                                                  new OrderSummary(307.0, 1.5),
                                                                  new OrderSummary(306.0, 5.5));

        //when
        List<OrderSummary> sellSummaryList = this.liveOrderBoard.getOrderSummary(SELL);
        List<OrderSummary> buySummaryList = this.liveOrderBoard.getOrderSummary(BUY);

        //then
        assertThat(sellSummaryList, contains(expectedSellOrderSummaryList.toArray()));
        assertThat(buySummaryList, contains(expectedBuyOrdersSummaryList.toArray()));
    }

    private void stubOrderNotFound(final Order order) {
        Mockito.doThrow(new OrderNorFoundExceptionFound()).when(this.orderRepository).delete(order);
    }
}
