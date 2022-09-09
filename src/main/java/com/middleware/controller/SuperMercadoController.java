package com.middleware.controller;

import com.middleware.constants.LogConstants;
import com.middleware.model.ProductsCatalogResponse;
import com.middleware.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

}
