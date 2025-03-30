package com.dailycodework.dreamshops.service.product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dailycodework.dreamshops.dto.ImageDto;
import com.dailycodework.dreamshops.dto.ProductDto;
import com.dailycodework.dreamshops.exception.AlreadyExistingException;
import com.dailycodework.dreamshops.exception.ProductNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import com.dailycodework.dreamshops.repository.ImageRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ModelMapper modelMapper;
	private final ImageRepository imageRepository;

	@Transactional
	@Override
	public Product addProduct(AddProductRequest request) {
		// Check if the category is found in the DB
		// If, Yes set it as the new product category
		// If, No the save it as a new category
		// The set as the new product category
		if (productExists(request.getName(), request.getBrand())) {
			throw new AlreadyExistingException(request.getBrand() + " " + request.getName() + "already exists, you may update this product instead !");
		}

		Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
				.orElseGet(() -> {

					Category newCategory = new Category(request.getCategory().getName());
					return categoryRepository.save(newCategory);

				});
		request.setCategory(category);
		return productRepository.save(createProduct(request, category));
	}

	private boolean productExists(String name, String brand) {
		return productRepository.existsByNameAndBrand(name, brand);
	}

	private Product createProduct(AddProductRequest request, Category category) {
		return new Product(request.getName(), request.getBrand(), request.getPrice(), request.getInventory(),
				request.getDescription(), category);
	}

	@Override
	public Product getProductById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
	}

	@Override
	public void deleteProductById(Long id) {
		productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
			throw new ProductNotFoundException("Product Not Found");
		});
	}

	@Override
	public Product updateProduct(ProductUpdateRequest request, Long productId) {
		return productRepository.findById(productId)
				.map(existingProduct -> updateExitingProduct(existingProduct, request)).map(productRepository::save)
				.orElseThrow(() -> new ProductNotFoundException("Product Not Found!"));
	}

	private Product updateExitingProduct(Product existingProduct, ProductUpdateRequest request) {
		existingProduct.setName(request.getName());
		existingProduct.setBrand(request.getBrand());
		existingProduct.setPrice(request.getPrice());
		existingProduct.setDescription(request.getDescription());
		existingProduct.setInventory(request.getInventory());

		Category category = categoryRepository.findByName(request.getCategory().getName());
		existingProduct.setCategory(category);
		return existingProduct;
	}

	@Override
	public List<Product> getAllProduct() {
		return productRepository.findAll();
	}

	@Override
	public List<Product> getProductsByCategory(String category) {
		return productRepository.findByCategoryName(category);
	}

	@Override
	public List<Product> getProductsByBrand(String brand) {
		return productRepository.findByBrand(brand);
	}

	@Override
	public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
		return productRepository.findByCategoryNameAndBrand(category, brand);
	}

	@Override
	public List<Product> getProductByName(String name) {
		return productRepository.findByName(name);
	}

	@Override
	public List<Product> getProductByBrandAndName(String brand, String name) {
		return productRepository.findByBrandAndName(brand, name);
	}

	@Override
	public Long countProductByBrandAndName(String brand, String name) {
		return productRepository.countByBrandAndName(brand, name);
	}

	@Override
	public List<ProductDto> getConvertedProduct(List<Product> products) {

		return products.stream().map(this::convertToDto).toList();
	}

	@Override
	public ProductDto convertToDto(Product product) {
		ProductDto productDto = modelMapper.map(product, ProductDto.class);

		List<Image> images = imageRepository.findByProductId(product.getId());

		List<ImageDto> imageDtos = images.stream().map(image -> modelMapper.map(image, ImageDto.class)).toList();
		productDto.setImages(imageDtos);

		return productDto;

	}

}
