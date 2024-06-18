package org.example.library.controller;

import org.example.library.model.Category;
import org.example.library.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    @GetMapping("/getAll")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
