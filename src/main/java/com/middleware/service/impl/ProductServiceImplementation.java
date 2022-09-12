package com.middleware.service.impl;

import com.middleware.config.ProductCatalogConfig;
import com.middleware.model.Response.ProductsCatalogResponse;
import com.middleware.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductCatalogConfig productCatalogConfig;

    @Override
    public ProductsCatalogResponse getCatalogProducts(){

        return new ProductsCatalogResponse(productCatalogConfig.getProduct());
    }

    public List<ProductCatalogConfig.Product> getProductsCatalog(){
        return productCatalogConfig.getProduct();
    }

    public Map<Integer,ProductCatalogConfig.Product> getProductsCatalogMapByIdProduct(){
        List<ProductCatalogConfig.Product> productList=productCatalogConfig.getProduct();

        Map<Integer, ProductCatalogConfig.Product> productMap = productList.stream().collect(Collectors.toMap(ProductCatalogConfig.Product::getId, product -> product));
        return productMap;
    }

    public Map<String,ProductCatalogConfig.Product> getProductsCatalogMapByNameProduct(){
        List<ProductCatalogConfig.Product> productList=productCatalogConfig.getProduct();
        Map<String, ProductCatalogConfig.Product> productsMap =
                productList.stream().collect(Collectors.toMap(ProductCatalogConfig.Product::getNombre, product -> product));

        return productsMap;
    }
}
