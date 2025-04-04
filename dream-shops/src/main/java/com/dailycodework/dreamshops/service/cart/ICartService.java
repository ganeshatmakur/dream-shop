package com.dailycodework.dreamshops.service.cart;

import java.math.BigDecimal;

import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.User;

public interface ICartService {
	Cart  getCartId(Long id);
	void clearCart(Long id);
	BigDecimal getTotalPrice(Long id);
	Cart getCartByUserId(Long userId);
	Cart intializeNewCart(User user);
}
