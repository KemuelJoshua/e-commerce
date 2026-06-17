package com.kemueljoshuamariano.ecommerce.category.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kemueljoshuamariano.ecommerce.category.dto.CategoryRequest;
import com.kemueljoshuamariano.ecommerce.category.model.Category;
import com.kemueljoshuamariano.ecommerce.category.repository.CategoryRepository;
import com.kemueljoshuamariano.ecommerce.category.service.CategoryService;
import com.kemueljoshuamariano.ecommerce.common.response.ErrorResponse;
import com.kemueljoshuamariano.ecommerce.common.response.Response;
import com.kemueljoshuamariano.ecommerce.common.response.SuccessResponse;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Response getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findByDeletedFalse();

            return new SuccessResponse(categories);
        } catch (Exception e) {
            return new ErrorResponse("Failed to fetch categories: " + e.getMessage(), 500);
        }
    }

    @Override
    @Transactional
    public Response createCategory(CategoryRequest categoryRequest) {
        try {
            Optional<Category> existingCategory =
                    categoryRepository.findByName(categoryRequest.getName().trim());

            if (existingCategory.isPresent()) {
                return new ErrorResponse(
                        "Category '" + categoryRequest.getName() + "' already exists.",
                        409
                );
            }

            Category category = new Category();
            category.setName(categoryRequest.getName().trim());
            category.setDescription(categoryRequest.getDescription());
            category.setDeleted(false);

            Category savedCategory = categoryRepository.save(category);

            return new SuccessResponse(savedCategory);

        } catch (Exception e) {
            return new ErrorResponse(e.getMessage(), 500);
        }
    }

    @Override
    public Response getCategoryById(Long id) {
        try {
            Optional<Category> category = categoryRepository.findById(id);

            if (category.isEmpty() || category.get().isDeleted()) {
                return new ErrorResponse("Category not found", 404);
            }

            return new SuccessResponse(category.get());
        } catch (Exception e) {
            return new ErrorResponse("Failed to fetch category", 500);
        }
    }

    @Override
    @Transactional
    public Response updateCategory(Long id, CategoryRequest categoryRequest) {
        try {
            Optional<Category> categoryOptional = categoryRepository.findById(id);

            if (categoryOptional.isEmpty() || categoryOptional.get().isDeleted()) {
                return new ErrorResponse("Category not found", 404);
            }

            Optional<Category> existingCategory = categoryRepository.findByName(categoryRequest.getName());
            if (existingCategory.isPresent()
                    && !existingCategory.get().getId().equals(id)
                    && !existingCategory.get().isDeleted()) {
                return new ErrorResponse("Category already exists", 409);
            }

            Category category = categoryOptional.get();
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());

            Category updatedCategory = categoryRepository.save(category);

            return new SuccessResponse(updatedCategory);
        } catch (Exception e) {
            return new ErrorResponse("Failed to update category", 500);
        }
    }

    @Override
    @Transactional
    public Response deleteCategory(Long id) {
        try {
            Optional<Category> category = categoryRepository.findById(id);

            if (category.isEmpty() || category.get().isDeleted()) {
                return new ErrorResponse("Category not found", 404);
            }

            Category existingCategory = category.get();
            existingCategory.setDeleted(true);
            categoryRepository.save(existingCategory);

            return new SuccessResponse("Category deleted successfully");
        } catch (Exception e) {
            return new ErrorResponse("Failed to delete category", 500);
        }
    }
}
