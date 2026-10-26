package com.ekjot.orderprocessing.config;

import com.ekjot.orderprocessing.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final ProductService productService;

    public DataLoader(ProductService productService){
       this.productService = productService;
    }

    @Override
    public void run(String... args){
//        productService.createProduct();
    }

}
