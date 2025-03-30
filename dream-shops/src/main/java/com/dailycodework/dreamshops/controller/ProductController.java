package com.dailycodework.dreamshops.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.dto.ProductDto;
import com.dailycodework.dreamshops.exception.AlreadyExistingException;
import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
	private final IProductService productService;

	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAllProducte() {
		List<Product> product = productService.getAllProduct();
		List<ProductDto> convertToDto = productService.getConvertedProduct(product);
		return ResponseEntity.ok(new ApiResponse("All Products", convertToDto));
	}

	@GetMapping("/product/{productId}/product")
	public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
		try {
			Product productById = productService.getProductById(productId);
			ProductDto convertToDto = productService.convertToDto(productById);
			return ResponseEntity.ok(new ApiResponse("Found", convertToDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addProducts(@RequestBody AddProductRequest product) {
		try {
			Product product2 = productService.addProduct(product);
			ProductDto productDto = productService.convertToDto(product2);
			return ResponseEntity.ok(new ApiResponse("Added Product Success", productDto));
		} catch (AlreadyExistingException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/product/{productId}/update")
	public ResponseEntity<ApiResponse> updateProductById(@PathVariable Long productId,
			@RequestBody ProductUpdateRequest updateRequest) {
		try {
			Product updateProduct = productService.updateProduct(updateRequest, productId);
			ProductDto productDto = productService.convertToDto(updateProduct);
			return ResponseEntity.ok(new ApiResponse("Updated Success", productDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/product/{productId}")
	public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long productId) {
		try {
			productService.deleteProductById(productId);
			return ResponseEntity.ok(new ApiResponse("Deleted Success", productId));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/product/by/brand-and-name")
	public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brand,
			@RequestParam String productName) {
		try {
			List<Product> product = productService.getProductByBrandAndName(brand, productName);
			List<ProductDto> convertedProduct = productService.getConvertedProduct(product);

			if (product.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("product  Not Found", null));
			}
			return ResponseEntity.ok(new ApiResponse("success", convertedProduct));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/product/by/category-and-brand")
	public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String category,
			@RequestParam String brand) {
		try {
			List<Product> productsByCategoryAndBrand = productService.getProductsByCategoryAndBrand(category, brand);
			List<ProductDto> convertedProduct = productService.getConvertedProduct(productsByCategoryAndBrand);
			if (productsByCategoryAndBrand.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND)
						.body(new ApiResponse("Product  Not Found", productsByCategoryAndBrand));
			}
			return ResponseEntity.ok(new ApiResponse("Success", convertedProduct));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/product/{name}/products")
	public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
		try {
			List<Product> productByName = productService.getProductByName(name);
			List<ProductDto> product = productService.getConvertedProduct(productByName);
			if (productByName.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product Not Found", null));
			}
			return ResponseEntity.ok(new ApiResponse("Success", product));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/product/by-brand")
	public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand) {
		try {
			List<Product> productsByBrand = productService.getProductsByBrand(brand);
			List<ProductDto> product = productService.getConvertedProduct(productsByBrand);

			if (productsByBrand.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("product are Not Found", null));
			}
			return ResponseEntity.ok(new ApiResponse("Product Found", product));
		} catch (Exception e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/product/{category}/all/products")
	public ResponseEntity<ApiResponse> findProductByCategory(@PathVariable String category) {
		try {
			List<Product> productsByCategory = productService.getProductsByCategory(category);
			List<ProductDto> product = productService.getConvertedProduct(productsByCategory);
			if (productsByCategory.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("product are Not Found", null));
			}
			return ResponseEntity.ok(new ApiResponse("Product Found", product));
		} catch (Exception e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/product/count/by-brand/and-name")
	public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand,
			@RequestParam String name) {
		try {
			var countProductByBrandAndName = productService.countProductByBrandAndName(brand, name);
			return ResponseEntity.ok(new ApiResponse("Product Count", countProductByBrandAndName));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
		}
	}
}
