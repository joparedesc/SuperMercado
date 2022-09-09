package com.middleware.service;

import com.middleware.config.ProductCatalogConfig;
import com.middleware.model.ProductsCatalogResponse;

import java.util.List;


public interface ProductService {
    //String getCatalogProducts();
    ProductsCatalogResponse getCatalogProducts();

}
