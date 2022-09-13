package com.middleware.service;

import com.middleware.model.DetailShoppingCart;
import com.middleware.model.Request.DeleteProductOfShoppingCartRequest;
import com.middleware.model.Request.UserShoppingCartRequest;
import com.middleware.model.ShoppingCart;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.util.Map;

public interface ShoppingService {

    Map<Integer, ShoppingCart> initialShoppingCart();

    ShoppingCart addProductToShoppingCart(
            int idUser, UserShoppingCartRequest userShoppingCartRequest);

    Map<Integer, ShoppingCart> addProductToShoppingCartMap(
            String idUser, UserShoppingCartRequest userShoppingCartRequest);

    ShoppingCart deleteProductToShoppingCart(
            int idUser, DeleteProductOfShoppingCartRequest deleteProductOfShoppingCartRequest);

    ShoppingCart getShoppingCartUserById(int idUser);

    ShoppingCart updateQuantityOfProductOfShoppingCart(
            int idUser,
            int idProduct,
            String nameProduct,
            int productQuantity
    );
}
