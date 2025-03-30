package com.dailycodework.dreamshops.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dailycodework.dreamshops.exception.AlreadyExistingException;
import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
	private final CategoryRepository categoryRepository;

	@Override
	public Category getCategoryById(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));
	}

	@Override
	public Category getCategoryByName(String name) {
		return categoryRepository.findByName(name);
	}

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category addCategory(Category category) {
		return Optional.of(category).filter(c -> !categoryRepository.existsByName(category.getName()))
				.map(categoryRepository::save)
				.orElseThrow(() -> new AlreadyExistingException(category.getName() + "Already Exit"));
	}

	@Override
	public Category updateCategory(Category category, Long id) {
		return Optional.ofNullable(getCategoryById(id)).map(oldcategory -> {
			oldcategory.setName(category.getName());
			return categoryRepository.save(oldcategory);
		}).orElseThrow(() -> new ResourceNotFoundException("category Not Found !"));
	}

	@Override
	public void deleteCategoryById(Long id) {

		categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {
			throw new ResourceNotFoundException("Category Not found");
		});
	}

}
