package com.dailycodework.dreamshops.service.order;

import java.util.List;

import com.dailycodework.dreamshops.dto.OrderDto;
import com.dailycodework.dreamshops.model.Order;

public interface IOrderService {
	Order placeOrder(Long userId);
	OrderDto getOrder(Long userId);
	List<OrderDto> getUserOrders(Long userId);
	OrderDto convertToDto(Order order);
}
