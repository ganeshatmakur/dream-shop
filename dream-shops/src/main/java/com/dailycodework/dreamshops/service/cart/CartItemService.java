package com.dailycodework.dreamshops.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.CartItem;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.CartItemRepository;
import com.dailycodework.dreamshops.repository.CartRepository;
import com.dailycodework.dreamshops.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
	private final CartItemRepository cartItemRepository;
	private final CartRepository cartRepository;
	private final IProductService productService;
	private final ICartService cartService;  

	@Override
	public void addItemToCart(Long cartId, Long productId, int quantity) {
		// 1. Get the Cart
		// 2.Get the product
		// 3.Check if the product already in the Cart
		// 4.If yes ,then increase the quantity with the requested quantity
		// 5.If No, the initiate a new cartItem entry
		Cart cart = cartService.getCartId(cartId);
		Product product = productService.getProductById(productId);
		CartItem cartItem = cart.getItems()
				.stream()
				.filter(iteam -> iteam.getProduct().getId().equals(productId))
				.findFirst().orElse(new CartItem());

		if (cartItem.getId() == null) {
			cartItem.setCart(cart);
			cartItem.setProduct(product);
			cartItem.setQuantity(quantity);
			cartItem.setUnitPrice(product.getPrice());

		} else {
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
		}
		cartItem.setTotalPrice();
		cart.addItem(cartItem);
		cartItemRepository.save(cartItem);
		cartRepository.save(cart);
	}

	@Override
	public void removeItemFromCart(Long cartId, Long productId) {
		Cart cart = cartService.getCartId(cartId);
		CartItem itemToRemove = getCartItem(cartId, productId);
		cart.removeItem(itemToRemove);
		cartRepository.save(cart);
	}

	@Override
	public void updateItemfromQuantity(Long cartId, Long productId, int quantity) {
		Cart cart = cartService.getCartId(cartId);
		cart.getItems()
				.stream()
						.filter(item -> item.getProduct().getId().equals(productId))
						.findFirst()
						.ifPresent(item -> {
							item.setQuantity(quantity);
							item.setUnitPrice(item.getProduct().getPrice());
							item.setTotalPrice();
				});
		BigDecimal totalAmount = cart.getItems()
				.stream().map(CartItem::getTotalPrice)
				.reduce(BigDecimal.ZERO,BigDecimal::add);
		cart.setTotalAmount(totalAmount);
		cartRepository.save(cart);
	}

	@Override
	public CartItem getCartItem(Long cartId, Long productId) {
		Cart cart = cartService.getCartId(cartId);
		return cart.getItems().stream()
				.filter(item -> item.getProduct().getId().equals(productId))
				.findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Item Not Found"));
	}
}
