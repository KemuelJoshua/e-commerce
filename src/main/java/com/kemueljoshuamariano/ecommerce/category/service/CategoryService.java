package com.kemueljoshuamariano.ecommerce.category.service;

import com.kemueljoshuamariano.ecommerce.category.dto.CategoryRequest;
import com.kemueljoshuamariano.ecommerce.common.response.Response;

public interface CategoryService {

    Response getAllCategories();
    
    Response createCategory(CategoryRequest categoryRequest);

    Response getCategoryById(Long id);

    Response updateCategory(Long id, CategoryRequest categoryRequest);

    Response deleteCategory(Long id);
    
}
