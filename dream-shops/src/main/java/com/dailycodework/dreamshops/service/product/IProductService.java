package com.dailycodework.dreamshops.service.product;

import java.util.List;

import com.dailycodework.dreamshops.dto.ProductDto;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;

public interface IProductService {
	Product addProduct(AddProductRequest request);
	Product getProductById(Long id);
	void deleteProductById(Long id);
	Product updateProduct(ProductUpdateRequest request, Long productId);
	List<Product> getAllProduct();
	List<Product> getProductsByCategory(String category);
	List<Product> getProductsByBrand(String brand);
	List<Product> getProductsByCategoryAndBrand(String category, String brand);
	List<Product> getProductByName(String name);
	List<Product> getProductByBrandAndName(String brand, String name);
	Long countProductByBrandAndName(String brand, String name);
	List<ProductDto> getConvertedProduct(List<Product> products);
	ProductDto convertToDto(Product product);

}
