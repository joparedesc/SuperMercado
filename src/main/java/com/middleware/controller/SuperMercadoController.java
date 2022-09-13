package com.middleware.controller;

import com.middleware.config.ProductCatalogConfig;
import com.middleware.constants.LogConstants;
import com.middleware.model.Request.DeleteProductOfShoppingCartRequest;
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

    /**
     * AddProductShoppingCart method.
     * Method to add product to shopping cart of a user.
     * @param idUser id user.
     * @param userShoppingCartRequest request user shopping cart.
     * @return ResponseEntity<ShoppingCart>
     */
    @PostMapping(
            path = "${controller.api-add-product-shopping-cart}",
            produces = "application/json",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseBody
    public ResponseEntity<ShoppingCart> addProductShoppingCart(
            @Valid @RequestBody UserShoppingCartRequest userShoppingCartRequest,
            @RequestHeader(value="${headers.id-user}") int idUser) {
        log.info(LogConstants.START_APPLICATION_ADD_PRODUCT_SHOPPING_CART,idUser);
        ShoppingCart shoppingCart = shoppingService.addProductToShoppingCart(idUser,userShoppingCartRequest);
        log.info(LogConstants.FINISH_APPLICATION_ADD_PRODUCT_SHOPPING_CART,idUser);
        return new ResponseEntity(shoppingCart, HttpStatus.OK);
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

    /**
     * DeleteProductShoppingCart method.
     * Method to add product to shopping cart of a user.
     * @param idUser id user.
     * @param deleteProductOfShoppingCartRequest request user shopping cart.
     * @return ResponseEntity<ShoppingCart>
     */
    @DeleteMapping(
            path = "${controller.api-delete-product-shopping-cart}",
            produces = "application/json",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseBody
    public ResponseEntity<ShoppingCart> deleteProductShoppingCart(
            @Valid @RequestBody DeleteProductOfShoppingCartRequest deleteProductOfShoppingCartRequest,
            @RequestHeader(value="${headers.id-user}") int idUser) {
        log.info(LogConstants.START_APPLICATION_DELETE_PRODUCT_SHOPPING_CART,idUser);
        ShoppingCart shoppingCart = shoppingService.deleteProductToShoppingCart(idUser,deleteProductOfShoppingCartRequest);
        log.info(LogConstants.FINISH_APPLICATION_DELETE_PRODUCT_SHOPPING_CART,idUser);
        return new ResponseEntity(shoppingCart, HttpStatus.OK);
    }

    /**
     * UpdateQuantityOfProductOfShoppingCart method.
     * Method to update quantity of product into shopping cart of a user.
     * @param idUser id user.
     * @param userShoppingCartRequest request user shopping cart.
     * @return ResponseEntity<ShoppingCart>
     */
    @PutMapping(
            path = "${controller.api-update-quantity-product-shopping-cart}",
            produces = "application/json",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseBody
    public ResponseEntity<ShoppingCart> updateQuantityOfProductOfShoppingCart(
            @Valid @RequestBody UserShoppingCartRequest userShoppingCartRequest,
            @RequestHeader(value="${headers.id-user}") int idUser) {
        log.info(LogConstants.START_APPLICATION_UPDATE_QUANTITY_OF_PRODUCT_OF_SHOPPING_CART,idUser);
        ShoppingCart shoppingCart = shoppingService.updateQuantityOfProductOfShoppingCart(idUser,userShoppingCartRequest);
        log.info(LogConstants.FINISH_APPLICATION_UPDATE_QUANTITY_OF_PRODUCT_OF_SHOPPING_CART,idUser);
        return new ResponseEntity(shoppingCart, HttpStatus.OK);
    }


}
