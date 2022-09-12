package com.middleware.service.impl;

import com.middleware.config.ProductCatalogConfig;
import com.middleware.constants.LogConstants;
import com.middleware.model.DetailShoppingCart;
import com.middleware.model.Request.UserShoppingCartRequest;
import com.middleware.model.ShoppingCart;
import com.middleware.model.User;
import com.middleware.service.ProductService;
import com.middleware.service.ShoppingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ShoppingServiceImplementation implements ShoppingService {

    @Autowired
    ProductService productService;

    public Map<Integer,ShoppingCart> shoppingCartMap;

    public Map<Integer, ShoppingCart> initialShoppingCart(){

        //Creating shoppingCart
        shoppingCartMap=new TreeMap<Integer,ShoppingCart>();

        //Creating listUsers
        User userJesus=new User(1,"Jesus","Paredes","Jesu");

        //Shopping cartJesus
        ShoppingCart shoppingCartJesus= new ShoppingCart();

        shoppingCartJesus.setIdShopping(1);
        shoppingCartJesus.setUser(userJesus);
        shoppingCartJesus.setDateShopping(new Date());


        //detail Shopping cart
        List<DetailShoppingCart> detailShoppingCartJesus = new ArrayList<DetailShoppingCart>();

        //Initial products catalog
        Map<Integer, ProductCatalogConfig.Product> productsMap = productService.getProductsCatalogMapByIdProduct();

        //Detail product one
        DetailShoppingCart detailShoppingCartOne=new DetailShoppingCart();
        detailShoppingCartOne.setIdDetailShoppingCart(1);
        detailShoppingCartOne.setProduct(productsMap.get(1));//Producto leche
        detailShoppingCartOne.setProductQuantity(3);
        detailShoppingCartOne.setTotalAmount(productsMap.get(1).getPrecio()*detailShoppingCartOne.getProductQuantity());

        //Detail product one
        DetailShoppingCart detailShoppingCartTwo=new DetailShoppingCart();
        detailShoppingCartTwo.setIdDetailShoppingCart(2);
        detailShoppingCartTwo.setProduct(productsMap.get(2));//Producto Pan Bimbo
        detailShoppingCartTwo.setProductQuantity(3);
        detailShoppingCartTwo.setTotalAmount(productsMap.get(2).getPrecio()*detailShoppingCartTwo.getProductQuantity());

        detailShoppingCartJesus.add(detailShoppingCartOne);
        detailShoppingCartJesus.add(detailShoppingCartTwo);

        shoppingCartJesus.setDetailShoppingCart(detailShoppingCartJesus);
        shoppingCartJesus.setTotalAmount(
                detailShoppingCartJesus.stream().mapToDouble(DetailShoppingCart::getTotalAmount).sum()
        );

        shoppingCartMap.put(userJesus.getIdUser(), shoppingCartJesus);

        return shoppingCartMap;
    }

    public ShoppingCart addProductToShoppingCart(int idUser, UserShoppingCartRequest userShoppingCartRequest){

        try{
            ShoppingCart shoppingCart= new ShoppingCart();
            if(initialShoppingCart().containsKey(idUser)){
                shoppingCart= initialShoppingCart().get(idUser);
                //Initial products catalog by id product
                Map<Integer, ProductCatalogConfig.Product> productsCatalogMapByIdProduct = productService.getProductsCatalogMapByIdProduct();
                //Initial products catalog by name product
                Map<String, ProductCatalogConfig.Product> productsCatalogMapByNameProduct = productService.getProductsCatalogMapByNameProduct();
                if(productsCatalogMapByIdProduct.containsKey(userShoppingCartRequest.getIdProduct())
                        ||productsCatalogMapByNameProduct.containsKey(userShoppingCartRequest.getNameProduct())){

                    OptionalInt maxIdDetailShoppingCartOpt=shoppingCart.getDetailShoppingCart().stream().mapToInt(
                            DetailShoppingCart::getIdDetailShoppingCart
                    ).max();

                    int maxIdDetailShoppingCart=
                            maxIdDetailShoppingCartOpt.isPresent()?maxIdDetailShoppingCartOpt.getAsInt()+1:
                            0;


                    //Product by id
                    if(productsCatalogMapByIdProduct.containsKey(userShoppingCartRequest.getIdProduct())){
                        shoppingCart.getDetailShoppingCart().add
                                (
                                        new DetailShoppingCart(
                                                maxIdDetailShoppingCart,
                                                productsCatalogMapByIdProduct.get(userShoppingCartRequest.getIdProduct()),
                                                userShoppingCartRequest.getProductQuantity(),
                                                productsCatalogMapByIdProduct.get(userShoppingCartRequest.getIdProduct()).getPrecio()*userShoppingCartRequest.getProductQuantity()
                                        )
                                );
                    }else{
                        shoppingCart.getDetailShoppingCart().add
                                (
                                        new DetailShoppingCart(
                                                maxIdDetailShoppingCart,
                                                productsCatalogMapByNameProduct.get(userShoppingCartRequest.getNameProduct()),
                                                userShoppingCartRequest.getProductQuantity(),
                                                productsCatalogMapByNameProduct.get(userShoppingCartRequest.getNameProduct()).getPrecio()*userShoppingCartRequest.getProductQuantity()
                                        )
                                );
                    }
                    shoppingCart.setTotalAmount(shoppingCart.getDetailShoppingCart().stream().mapToDouble(detailShopping->detailShopping.getTotalAmount()).sum());

                }else{
                    throw new RuntimeException(
                            String.format(
                                    "Product is not present with id product : [%d]", userShoppingCartRequest.getIdProduct()
                                    +"or Product is not present with name product :  [%s]",userShoppingCartRequest.getNameProduct()

                            )
                    );
                }
            }else{
                throw new RuntimeException(
                        String.format("Shopping cart is not present with User id: [%d]", idUser)
                );
            }
            return shoppingCart;
        }catch (RuntimeException exception){
            log.error(LogConstants.SOMETHING_WENT_WRONG_WHILE_ADD_PRODUCT_TO_SHOPPING_CART,idUser,exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    public Map<Integer, ShoppingCart> addProductToShoppingCartMap(String idUser, UserShoppingCartRequest userShoppingCartRequest){

        ShoppingCart shoppingCart= new ShoppingCart();
        Map<Integer,ShoppingCart> shoppingCartMap= new TreeMap<>();

        return shoppingCartMap;
    }

    public ShoppingCart getShoppingCartUserById(int idUser){

        return initialShoppingCart().get(idUser);

    }


}
