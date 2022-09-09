package com.middleware.service.impl;

import com.middleware.config.ProductCatalogConfig;
import com.middleware.model.ProductsCatalogResponse;
import com.middleware.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductCatalogConfig productCatalogConfig;



    /*
    public String getCatalogProducts() {
        return productCatalogConfig.getProduct().toString();
    }
    */
    @Override
    public ProductsCatalogResponse getCatalogProducts(){

        return new ProductsCatalogResponse(productCatalogConfig.getProduct());
    }
}
