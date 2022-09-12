package com.middleware.service;

import com.middleware.config.ProductCatalogConfig;
import com.middleware.model.Response.ProductsCatalogResponse;

import java.util.List;
import java.util.Map;


public interface ProductService {
    ProductsCatalogResponse getCatalogProducts();

    List<ProductCatalogConfig.Product> getProductsCatalog();

    Map<Integer,ProductCatalogConfig.Product> getProductsCatalogMapByIdProduct();

    Map<String,ProductCatalogConfig.Product> getProductsCatalogMapByNameProduct();

}
