package com.middleware.controller;

import com.middleware.constants.LogConstants;
import com.middleware.model.Request.DeleteProductOfShoppingCartRequest;
import com.middleware.model.Request.UserShoppingCartRequest;
import com.middleware.model.Response.ProductsCatalogResponse;
import com.middleware.model.Response.RestTemplateResponse;
import com.middleware.model.ShoppingCart;
import com.middleware.service.RestTemplateService;
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
import java.util.List;



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

    @Autowired
    RestTemplateService restTemplateService;

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
     * @param idProduct
     * @param nameProduct
     * @param productQuantity
     * @return ResponseEntity<ShoppingCart>
     */
    @PutMapping(
            path = "${controller.api-update-quantity-product-shopping-cart}",
            produces = "application/json"
    )
    @ResponseBody
    public ResponseEntity<ShoppingCart> updateQuantityOfProductOfShoppingCart(
            @Valid @PathVariable(required = false) int idProduct,
            @Valid @PathVariable(required = false) String nameProduct,
            @Valid @PathVariable(required = true) int productQuantity,
            @RequestHeader(value="${headers.id-user}") int idUser) {
        log.info(LogConstants.START_APPLICATION_UPDATE_QUANTITY_OF_PRODUCT_OF_SHOPPING_CART,idUser);
        ShoppingCart shoppingCart = shoppingService.updateQuantityOfProductOfShoppingCart(
                idUser,idProduct,nameProduct,productQuantity);
        log.info(LogConstants.FINISH_APPLICATION_UPDATE_QUANTITY_OF_PRODUCT_OF_SHOPPING_CART,idUser);
        return new ResponseEntity(shoppingCart, HttpStatus.OK);
    }

    /**
     * GetListResTemplateResponse method.
     * Method to consult a list of Rest template response.
     * @return ResponseEntity<List<RestTemplateResponse>> list of Rest template response
     */
    @GetMapping(
            path = "${controller.api-get-list-rest-template-response}",
            produces = "application/json"
    )
    @ResponseBody
    public ResponseEntity<List<RestTemplateResponse>> getListResTemplateResponse() {
        List<RestTemplateResponse> restTemplateResponseList = restTemplateService.getListResTemplateResponse();
        return new ResponseEntity(restTemplateResponseList, HttpStatus.OK);
    }


}
