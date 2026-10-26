package com.ekjot.orderprocessing.service;

import com.ekjot.orderprocessing.dto.request.CreateProductRequest;
import com.ekjot.orderprocessing.dto.request.UpdateProductRequest;
import com.ekjot.orderprocessing.dto.response.ProductResponse;
import com.ekjot.orderprocessing.entity.Product;
import com.ekjot.orderprocessing.enums.ProductStatus;
import com.ekjot.orderprocessing.exception.ProductNotFoundException;
import com.ekjot.orderprocessing.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    private ProductResponse mapToProductResponse(Product product){
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getStatus());
    }

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(CreateProductRequest request){
        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());

        product.setStatus(ProductStatus.ACTIVE);

        productRepository.save(product);

        return mapToProductResponse(product);
    }

    public ProductResponse getProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        return mapToProductResponse(product);
    }

    public ProductResponse updateProduct(Long id, UpdateProductRequest request){
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setStatus(request.getStatus());

        productRepository.save(product);

        return mapToProductResponse(product);
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
//        productRepository.deleteById(id);
    }
}
