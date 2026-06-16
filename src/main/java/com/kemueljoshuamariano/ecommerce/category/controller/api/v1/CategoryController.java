package com.kemueljoshuamariano.ecommerce.category.controller.api.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kemueljoshuamariano.ecommerce.category.dto.CategoryRequest;
import com.kemueljoshuamariano.ecommerce.category.service.CategoryService;
import com.kemueljoshuamariano.ecommerce.common.response.Response;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.createCategory(categoryRequest));
    }


}
