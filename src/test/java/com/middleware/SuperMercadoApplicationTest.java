package com.middleware;

import com.google.gson.Gson;
import com.middleware.config.ProductCatalogConfig;
import com.middleware.model.Response.ProductsCatalogResponse;
import com.middleware.model.ShoppingCart;
import com.middleware.service.ProductService;
import com.middleware.service.ShoppingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;


@SpringBootTest
class SuperMercadoApplicationTest {
    @Autowired
    ProductService productService;

    @Autowired
    ShoppingService shoppingService;

    @Test
    void main() {
        System.out.println("Metodo main()");
    }

    @Test
    void getCatalogProducts(){
        ProductsCatalogResponse productsCatalogResponse= productService.getCatalogProducts();
        productsCatalogResponse.getProduct().forEach(product->System.out.println(product.getNombre()));
    }

    @Test
    void getProductsCatalogMap() {

        Map<Integer, ProductCatalogConfig.Product> productsMap = productService.getProductsCatalogMapByIdProduct();

        ProductCatalogConfig.Product product=productsMap.get(1);

        System.out.println(product.getNombre());
    }

    @Test
    void showShoppingCart(){
        Map<Integer, ShoppingCart>  shoppingCartMap=shoppingService.initialShoppingCart();

        Gson gson = new Gson();
        String jsonShoppingCartMap =gson.toJson(shoppingCartMap.get(1));
        System.out.println("Shopping Cart: "+jsonShoppingCartMap);
    }

    @Test
    void getShoppingCart(){
        ShoppingCart shoppingCart=shoppingService.getShoppingCartUserById(1);
        Gson gson = new Gson();
        String jsonShoppingCartMap =gson.toJson(shoppingCart);
        System.out.println("Shopping Cart: "+jsonShoppingCartMap);
    }

}