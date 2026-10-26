package com.ekjot.orderprocessing.controller;

import com.ekjot.orderprocessing.dto.request.CreateProductRequest;
import com.ekjot.orderprocessing.dto.request.UpdateProductRequest;
import com.ekjot.orderprocessing.dto.response.ProductResponse;
import com.ekjot.orderprocessing.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request){
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable Long id){
        return productService.getProduct(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request){
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.noContent().build();
    }
}
