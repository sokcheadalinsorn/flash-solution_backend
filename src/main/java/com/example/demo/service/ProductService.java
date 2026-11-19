
package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.Brand;
import com.example.demo.model.Category;

import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.CategoryRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    // Constructor Injection
    public ProductService(
            ProductRepository productRepository,
            BrandRepository brandRepository,
            CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    // 1. Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 2. Get product by ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with ID: " + id));
    }

    // 3. Create product
    public Product createProduct(Product product) {

        Long brandId = product.getBrand().getId();
        Long categoryId = product.getCategory().getId();

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() ->
                        new RuntimeException("Brand not found with ID: " + brandId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with ID: " + categoryId));

        product.setBrand(brand);
        product.setCategory(category);

        return productRepository.save(product);
    }

    // 4. Update product
    public Product updateProduct(Long id, Product productDetails) {

        Product product = getProductById(id);

        Brand brand = brandRepository.findById(
                productDetails.getBrand().getId())
                .orElseThrow(() ->
                        new RuntimeException("Brand not found"));

        Category category = categoryRepository.findById(
                productDetails.getCategory().getId())
                .orElseThrow(() ->
                        new RuntimeException("Category not found"));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setBrand(brand);
        product.setCategory(category);
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setImageUrl(productDetails.getImageUrl());
        product.setSku(productDetails.getSku());

        return productRepository.save(product);
    }

    // 5. Delete product
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
