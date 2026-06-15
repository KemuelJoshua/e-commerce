package com.kemueljoshuamariano.ecommerce.category.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kemueljoshuamariano.ecommerce.category.dto.CategoryRequest;
import com.kemueljoshuamariano.ecommerce.category.model.Category;
import com.kemueljoshuamariano.ecommerce.category.repository.CategoryRepository;
import com.kemueljoshuamariano.ecommerce.category.service.CategoryService;
import com.kemueljoshuamariano.ecommerce.common.exception.Error;
import com.kemueljoshuamariano.ecommerce.common.response.Response;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Response getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll()
                    .stream()
                    .filter(category -> !category.isDeleted())
                    .toList();

            return new Response("success", categories, null);
        } catch (Exception e) {
            return new Response("failed", new Error("Failed to fetch categories", 500));
        }
    }

    @Override
    @Transactional
    public Response createCategory(CategoryRequest categoryRequest) {
        try {
            Optional<Category> existingCategory = categoryRepository.findByName(categoryRequest.getName());

            if (existingCategory.isPresent() && !existingCategory.get().isDeleted()) {
                return new Response("failed", new Error("Category already exists", 409));
            }

            Category category = existingCategory.orElseGet(Category::new);
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());
            category.setDeleted(false);

            Category savedCategory = categoryRepository.save(category);

            return new Response("success", savedCategory, null);
        } catch (Exception e) {
            return new Response("failed", new Error("Failed to create category", 500));
        }
    }

    @Override
    public Response getCategoryById(Long id) {
        try {
            Optional<Category> category = categoryRepository.findById(id);

            if (category.isEmpty() || category.get().isDeleted()) {
                return new Response("failed", new Error("Category not found", 404));
            }

            return new Response("success", category.get(), null);
        } catch (Exception e) {
            return new Response("failed", new Error("Failed to fetch category", 500));
        }
    }

    @Override
    @Transactional
    public Response updateCategory(Long id, CategoryRequest categoryRequest) {
        try {
            Optional<Category> categoryOptional = categoryRepository.findById(id);

            if (categoryOptional.isEmpty() || categoryOptional.get().isDeleted()) {
                return new Response("failed", new Error("Category not found", 404));
            }

            Optional<Category> existingCategory = categoryRepository.findByName(categoryRequest.getName());
            if (existingCategory.isPresent()
                    && !existingCategory.get().getId().equals(id)
                    && !existingCategory.get().isDeleted()) {
                return new Response("failed", new Error("Category already exists", 409));
            }

            Category category = categoryOptional.get();
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());

            Category updatedCategory = categoryRepository.save(category);

            return new Response("success", updatedCategory, null);
        } catch (Exception e) {
            return new Response("failed", new Error("Failed to update category", 500));
        }
    }

    @Override
    @Transactional
    public Response deleteCategory(Long id) {
        try {
            Optional<Category> category = categoryRepository.findById(id);

            if (category.isEmpty() || category.get().isDeleted()) {
                return new Response("failed", new Error("Category not found", 404));
            }

            Category existingCategory = category.get();
            existingCategory.setDeleted(true);
            categoryRepository.save(existingCategory);

            return new Response("success", "Category deleted successfully", null);
        } catch (Exception e) {
            return new Response("failed", new Error("Failed to delete category", 500));
        }
    }
}
