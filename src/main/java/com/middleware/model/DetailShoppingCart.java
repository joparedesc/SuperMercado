package com.middleware.model;

import com.middleware.config.ProductCatalogConfig;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetailShoppingCart {

    private int idDetailShoppingCart;

    private ProductCatalogConfig.Product product;

    private int productQuantity;

    private Double totalAmount;
}
