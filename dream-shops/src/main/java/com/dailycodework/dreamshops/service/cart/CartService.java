package com.dailycodework.dreamshops.service.cart;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.CartItemRepository;
import com.dailycodework.dreamshops.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;

	@Override
	public Cart getCartId(Long id) {
		Cart cart = cartRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));
		BigDecimal totalAmount = cart.getTotalAmount();
		cart.setTotalAmount(totalAmount);
		return cartRepository.save(cart);
	}
	@Transactional
	@Override
	public void clearCart(Long id) {
		Cart cart = getCartId(id);
		cartItemRepository.deleteAllByCartId(id);
		cartRepository.deleteById(id);
		cart.getItems().clear();
		cartRepository.deleteById(id);
	}

	@Override
	public BigDecimal getTotalPrice(Long id) {
		Cart cart = getCartId(id);
		return cart.getTotalAmount();
	}

	@Override
	public Cart intializeNewCart(User user) {
		return Optional.ofNullable(getCartByUserId(user.getId()))
				.orElseGet(() -> {
					Cart cart=new Cart();
					cart.setUser(user);
					return cartRepository.save(cart);
				});
		
	}
	@Override
	public  Cart getCartByUserId(Long userId) {
		return cartRepository.findByUserId(userId);
	}

}
