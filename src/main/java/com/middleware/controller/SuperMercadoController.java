package com.middleware.controller;

import com.middleware.config.ProductCatalogConfig;
import com.middleware.constants.LogConstants;
import com.middleware.model.Request.UserShoppingCartRequest;
import com.middleware.model.Response.ProductsCatalogResponse;
import com.middleware.model.ShoppingCart;
import com.middleware.service.ShoppingService;
import com.middleware.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;



/**
 * SuperMercadoController controller class.
 * @author jesu_
 */
@Slf4j
@RestController
@RequestMapping("${controller.base-path}")
public class SuperMercadoController {

    @Autowired
    ProductService productService;

    @Autowired
    ShoppingService shoppingService;

    /**
     * GetCatalogProducts method.
     * Method to consult the product catalog.
     *
     * @return ResponseEntity<ProductsCatalogResponse>
     */
    @GetMapping(
            path = "${controller.api-get-catalog-products}",
            produces = "application/json"
    )
    public ResponseEntity<ProductsCatalogResponse> getCatalogProducts() {
        log.debug(LogConstants.START_APPLICATION_GET_CATALOG_PRODUCTS);
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        ProductsCatalogResponse  catalogoProducts=productService.getCatalogProducts();
        log.debug(LogConstants.FINISH_APPLICATION_GET_CATALOG_PRODUCTS);
        return new ResponseEntity<ProductsCatalogResponse>(catalogoProducts, HttpStatus.OK);
    }

    @GetMapping(
            path = "${controller.api-get-catalog-products-map-by-id}",
            produces = "application/json"
    )
    @ResponseBody
    public ResponseEntity<Map<Integer, ProductCatalogConfig.Product>> getProductsCatalogMapByIdProduct() {
        Map<Integer,ProductCatalogConfig.Product> productMap = productService.getProductsCatalogMapByIdProduct();

        return new ResponseEntity<Map<Integer,ProductCatalogConfig.Product>>(productMap, HttpStatus.OK);
    }

    @GetMapping(
            path = "${controller.api-get-catalog-products-map-by-name}",
            produces = "application/json"
    )
    @ResponseBody
    public ResponseEntity<Map<String, ProductCatalogConfig.Product>> getProductsCatalogMapByNameProduct() {
        Map<String,ProductCatalogConfig.Product> productMap = productService.getProductsCatalogMapByNameProduct();

        return new ResponseEntity<Map<String,ProductCatalogConfig.Product>>(productMap, HttpStatus.OK);
    }


    @PostMapping(
            path = "${controller.api-add-product-shopping-cart}",
            produces = "application/json",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseBody
    public ResponseEntity<ShoppingCart> addProductShoppingCart(
            @Valid @RequestBody UserShoppingCartRequest userShoppingCartRequest,
            @RequestHeader(value="${headers.id-user}") int idUser) {
        log.info(LogConstants.START_APPLICATION_ADD_PRODUCT_SHOPPING_CART);
        ShoppingCart shoppingCart = shoppingService.addProductToShoppingCart(idUser,userShoppingCartRequest);
        log.info(LogConstants.FINISH_APPLICATION_ADD_PRODUCT_SHOPPING_CART);
        return new ResponseEntity(shoppingCart, HttpStatus.OK); // you can change status code based on response
    }

    /**
     * GetShoppingCartUserById method.
     * Method to consult the shopping cart by user id.
     *
     * @return ResponseEntity<ShoppingCart>
     */
    @GetMapping(
            path = "${controller.api-get-shopping-cart-by-user}",
            produces = "application/json"
    )
    public ResponseEntity<ShoppingCart> getShoppingCartUserById(
            @RequestHeader(value="${headers.id-user}",required = true) int idUser) {
        log.debug(LogConstants.START_APPLICATION_GET_SHOPPING_CART_BY_USER_ID,idUser);

        ShoppingCart  shoppingCart=shoppingService.getShoppingCartUserById(idUser);
        log.debug(LogConstants.FINISH_APPLICATION_GET_SHOPPING_CART_BY_USER_ID,idUser);

        return new ResponseEntity<ShoppingCart>(shoppingCart, HttpStatus.OK);
    }


}
