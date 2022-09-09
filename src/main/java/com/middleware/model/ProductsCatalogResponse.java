package com.middleware.model;

import com.middleware.config.ProductCatalogConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductsCatalogResponse {

    private List<ProductCatalogConfig.Product> product;
}
