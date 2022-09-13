package com.middleware.service.impl;

import com.middleware.DAO.SaveProductDAO;
import com.middleware.config.ProductCatalogConfig;
import com.middleware.constants.LogConstants;
import com.middleware.model.DetailShoppingCart;
import com.middleware.model.Request.DeleteProductOfShoppingCartRequest;
import com.middleware.model.Request.UserShoppingCartRequest;
import com.middleware.model.ShoppingCart;
import com.middleware.model.User;
import com.middleware.service.ProductService;
import com.middleware.service.ShoppingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShoppingServiceImplementation implements ShoppingService {

    @Autowired
    ProductService productService;

    @Autowired
    SaveProductDAO saveProductDAO;


    public Map<Integer,ShoppingCart> shoppingCartMap;

    public Map<Integer, ShoppingCart> initialShoppingCart(){

        //Creating shoppingCart
        shoppingCartMap=new TreeMap<Integer,ShoppingCart>();

        /* Creating listUsers */
        User userJesus=new User(1,"Jesus","Paredes","Jesu");

        //Shopping cartJesus
        ShoppingCart shoppingCartJesus= new ShoppingCart();

        shoppingCartJesus.setIdShopping(1);
        shoppingCartJesus.setUser(userJesus);
        shoppingCartJesus.setDateShopping(new Date());


        //detail Shopping cart
        List<DetailShoppingCart> detailShoppingCartJesus = new ArrayList<DetailShoppingCart>();

        /* Initial products catalog */
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
            //Message sent async
            saveProductDAO.sendMessageSaveProduct();
            ShoppingCart shoppingCart;
            if(initialShoppingCart().containsKey(idUser)){
                shoppingCart= initialShoppingCart().get(idUser);
                //Initial products catalog by id product
                Map<Integer, ProductCatalogConfig.Product> productsCatalogMapByIdProduct =
                        productService.getProductsCatalogMapByIdProduct();
                //Initial products catalog by name product
                Map<String, ProductCatalogConfig.Product> productsCatalogMapByNameProduct =
                        productService.getProductsCatalogMapByNameProduct();

                //If the product catalog contains the product by id or name
                if(productsCatalogMapByIdProduct.containsKey(userShoppingCartRequest.getIdProduct())
                        ||productsCatalogMapByNameProduct.containsKey(userShoppingCartRequest.getNameProduct())){

                    //new id detail of new product added to Shopping cart
                    OptionalInt maxIdDetailShoppingCartOpt=shoppingCart.getDetailShoppingCart().stream().mapToInt(
                            DetailShoppingCart::getIdDetailShoppingCart
                    ).max();

                    int maxIdDetailShoppingCart=
                            maxIdDetailShoppingCartOpt.isPresent()?maxIdDetailShoppingCartOpt.getAsInt()+1:
                            0;


                    //Product by id
                    if(productsCatalogMapByIdProduct.containsKey(userShoppingCartRequest.getIdProduct())){

                        //if Product Quantity>0
                        if(userShoppingCartRequest.getProductQuantity()>0){
                            shoppingCart.getDetailShoppingCart().add
                                    (
                                            new DetailShoppingCart(
                                                    maxIdDetailShoppingCart,
                                                    productsCatalogMapByIdProduct.get(
                                                            userShoppingCartRequest.getIdProduct()),
                                                    userShoppingCartRequest.getProductQuantity(),
                                                    productsCatalogMapByIdProduct.get(
                                                                    userShoppingCartRequest.getIdProduct())
                                                            .getPrecio()*userShoppingCartRequest.getProductQuantity()
                                            )
                                    );
                        }else{
                            throw new RuntimeException(
                                    String.format("Product quantity [%d] is not greater than zero.", userShoppingCartRequest.getProductQuantity())
                            );
                        }

                    }else{
                        //Product by name
                        if(userShoppingCartRequest.getProductQuantity()>0){
                            shoppingCart.getDetailShoppingCart().add
                                    (
                                            new DetailShoppingCart(
                                                    maxIdDetailShoppingCart,
                                                    productsCatalogMapByNameProduct.get(
                                                            userShoppingCartRequest.getNameProduct()),
                                                    userShoppingCartRequest.getProductQuantity(),
                                                    productsCatalogMapByNameProduct.get(
                                                                    userShoppingCartRequest.getNameProduct())
                                                            .getPrecio()*userShoppingCartRequest.getProductQuantity()
                                            )
                                    );
                        }else{
                            throw new RuntimeException(
                                    String.format("Product quantity [%d] is not greater than zero.", userShoppingCartRequest.getProductQuantity())
                            );
                        }

                    }
                    shoppingCart.setTotalAmount(
                            shoppingCart.getDetailShoppingCart().stream().mapToDouble(
                                    DetailShoppingCart::getTotalAmount).sum()
                    );

                }else{
                    String messageException=
                            "Product is not present with id product : "+
                                    userShoppingCartRequest.getIdProduct()+
                                    " or with name product :  "+
                                    userShoppingCartRequest.getNameProduct();
                    throw new RuntimeException(
                            messageException
                    );
                }
            }else{
                throw new RuntimeException(
                        String.format("Shopping cart is not present with User id: [%d]", idUser)
                );
            }
            return shoppingCart;
        }catch (RuntimeException exception){
            log.error(LogConstants.SOMETHING_WENT_WRONG_WHILE_ADD_PRODUCT_TO_SHOPPING_CART,
                    idUser,exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    public ShoppingCart deleteProductToShoppingCart(
            int idUser,
            DeleteProductOfShoppingCartRequest deleteProductOfShoppingCartRequest){

        try{
            ShoppingCart shoppingCart= new ShoppingCart();
            if(initialShoppingCart().containsKey(idUser)){
                shoppingCart= initialShoppingCart().get(idUser);

                //Initial products catalog by id product
                Map<Integer, ProductCatalogConfig.Product> productsCatalogMapByIdProduct =
                        productService.getProductsCatalogMapByIdProduct();
                //Initial products catalog by name product
                Map<String, ProductCatalogConfig.Product> productsCatalogMapByNameProduct =
                        productService.getProductsCatalogMapByNameProduct();

                //If the product catalog not contains the product by id or name
                if (!productsCatalogMapByIdProduct.containsKey(
                        deleteProductOfShoppingCartRequest.getIdProduct())
                        && !productsCatalogMapByNameProduct.containsKey(
                        deleteProductOfShoppingCartRequest.getNameProduct())) {
                    String messageException=
                                "Product is not present with id product : "+
                                        deleteProductOfShoppingCartRequest.getIdProduct()+
                                        " or with name product :  "+
                                        deleteProductOfShoppingCartRequest.getNameProduct();
                        throw new RuntimeException(
                                messageException
                        );
                } else {

                    //Product by id
                    if(productsCatalogMapByIdProduct.containsKey(
                            deleteProductOfShoppingCartRequest.getIdProduct())){

                            Optional<DetailShoppingCart> detailShoppingCartOptional = shoppingCart.getDetailShoppingCart().stream()
                                    .filter( productShopping-> productShopping.getProduct().getId()==deleteProductOfShoppingCartRequest.getIdProduct())
                                    .findFirst();
                            if(detailShoppingCartOptional.isPresent()){
                                //detail Shopping cart
                                List<DetailShoppingCart> detailShoppingCartNew = shoppingCart.getDetailShoppingCart().stream()
                                        .filter(productShopping->(productShopping.getProduct().getId())!=deleteProductOfShoppingCartRequest.getIdProduct())
                                        .collect(Collectors.toList());

                                shoppingCart.setDetailShoppingCart(detailShoppingCartNew);
                            }else {
                                throw new RuntimeException(
                                        String.format("Product id [%d] is not present into shopping cart of user.", deleteProductOfShoppingCartRequest.getIdProduct())
                                );
                            }

                        }else{
                            //Product by name
                            Optional<DetailShoppingCart> detailShoppingCartOptional = shoppingCart.getDetailShoppingCart().stream()
                                .filter( productShopping-> productShopping.getProduct().getNombre().equals(deleteProductOfShoppingCartRequest.getNameProduct()))
                                .findFirst();

                            if(detailShoppingCartOptional.isPresent()){
                                //detail Shopping cart
                                List<DetailShoppingCart> detailShoppingCartNew = shoppingCart.getDetailShoppingCart().stream()
                                        .filter(productShopping->(!productShopping.getProduct().getNombre().equals(deleteProductOfShoppingCartRequest.getNameProduct())))
                                        .collect(Collectors.toList());

                                shoppingCart.setDetailShoppingCart(detailShoppingCartNew);
                            }else {
                                throw new RuntimeException(
                                        String.format("Product name [%s] is not present into shopping cart of user.", deleteProductOfShoppingCartRequest.getNameProduct())
                                );
                            }

                        }

                        shoppingCart.setTotalAmount(
                                shoppingCart.getDetailShoppingCart().stream().mapToDouble(
                                        DetailShoppingCart::getTotalAmount).sum()
                        );

                }
            }else{
                throw new RuntimeException(
                        String.format("Shopping cart is not present with User id: [%d]", idUser)
                );
            }
            return shoppingCart;
        }catch (RuntimeException exception){
            log.error(LogConstants.SOMETHING_WENT_WRONG_WHILE_DELETE_PRODUCT_TO_SHOPPING_CART,
                    idUser,exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    public ShoppingCart updateQuantityOfProductOfShoppingCart(
            int idUser,
            int idProduct,
            String nameProduct,
            int productQuantity){

        try{
            ShoppingCart shoppingCart= new ShoppingCart();
            if(initialShoppingCart().containsKey(idUser)){
                shoppingCart= initialShoppingCart().get(idUser);

                //Initial products catalog by id product
                Map<Integer, ProductCatalogConfig.Product> productsCatalogMapByIdProduct =
                        productService.getProductsCatalogMapByIdProduct();
                //Initial products catalog by name product
                Map<String, ProductCatalogConfig.Product> productsCatalogMapByNameProduct =
                        productService.getProductsCatalogMapByNameProduct();

                //If the product catalog not contains the product by id or name
                if (!productsCatalogMapByIdProduct.containsKey(
                        idProduct)
                        && !productsCatalogMapByNameProduct.containsKey(
                        nameProduct)) {
                    String messageException=
                            "Product is not present with id product : "+
                                    idProduct+
                                    " or with name product :  "+
                                    nameProduct+
                            " into shopping cart.";
                    throw new RuntimeException(
                            messageException
                    );
                } else {

                    //Product by id
                    if(productsCatalogMapByIdProduct.containsKey(
                            idProduct)){

                        Optional<DetailShoppingCart> detailShoppingCartOptional = shoppingCart.getDetailShoppingCart().stream()
                                .filter( productShopping-> productShopping.getProduct().getId()==idProduct)
                                .findFirst();


                        if(detailShoppingCartOptional.isPresent()){
                            //if Product Quantity>0
                            if(productQuantity>0){
                                //Update of Product Quantity
                                detailShoppingCartOptional.get().setProductQuantity(productQuantity);
                                //Update total amount for product
                                detailShoppingCartOptional.get().setTotalAmount(
                                        detailShoppingCartOptional.get().getProductQuantity()*
                                                detailShoppingCartOptional.get().getProduct().getPrecio());
                            }else{
                                throw new RuntimeException(
                                        String.format("Product quantity [%d] is not greater than zero.", productQuantity)
                                );
                            }
                            //detail Shopping cart
                            List<DetailShoppingCart> detailShoppingCartNew = shoppingCart.getDetailShoppingCart().stream()
                                    .filter(productShopping->(productShopping.getProduct().getId())!=idProduct)
                                    .collect(Collectors.toList());

                            detailShoppingCartNew.add(detailShoppingCartOptional.get());

                            shoppingCart.setDetailShoppingCart(detailShoppingCartNew);
                        }else {
                            throw new RuntimeException(
                                    String.format("Product id [%d] is not present into shopping cart of user.", idProduct)
                            );
                        }

                    }else{
                        //Product by name
                        Optional<DetailShoppingCart> detailShoppingCartOptional = shoppingCart.getDetailShoppingCart().stream()
                                .filter( productShopping-> productShopping.getProduct().getNombre().equals(nameProduct))
                                .findFirst();

                        if(detailShoppingCartOptional.isPresent()){
                            //if Product Quantity>0
                            if(productQuantity>0){
                                //Update of Product Quantity
                                detailShoppingCartOptional.get().setProductQuantity(
                                        productQuantity);
                                //Update total amount for product
                                detailShoppingCartOptional.get().setTotalAmount(
                                        detailShoppingCartOptional.get().getProductQuantity()*
                                                detailShoppingCartOptional.get().getProduct().getPrecio());
                            }else{
                                throw new RuntimeException(
                                        String.format("Product quantity [%d] is not greater than zero.",
                                                productQuantity)
                                );
                            }
                            //detail Shopping cart
                            List<DetailShoppingCart> detailShoppingCartNew =
                                    shoppingCart.getDetailShoppingCart().stream()
                                    .filter(productShopping->(!productShopping.getProduct().getNombre().equals(
                                            nameProduct)))
                                    .collect(Collectors.toList());

                            detailShoppingCartNew.add(detailShoppingCartOptional.get());

                            shoppingCart.setDetailShoppingCart(detailShoppingCartNew);
                        }else {
                            throw new RuntimeException(
                                    String.format("Product name [%s] is not present into shopping cart of user.",
                                            nameProduct)
                            );
                        }

                    }

                    shoppingCart.setTotalAmount(
                            shoppingCart.getDetailShoppingCart().stream().mapToDouble(
                                    DetailShoppingCart::getTotalAmount).sum()
                    );

                }
            }else{
                throw new RuntimeException(
                        String.format("Shopping cart is not present with User id: [%d]", idUser)
                );
            }
            return shoppingCart;
        }catch (RuntimeException exception){
            log.error(LogConstants.SOMETHING_WENT_WRONG_WHILE_UPDATE_QUANTITY_OF_PRODUCT_OF_SHOPPING_CART,
                    idUser,exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    public Map<Integer, ShoppingCart> addProductToShoppingCartMap(
            String idUser, UserShoppingCartRequest userShoppingCartRequest){

        ShoppingCart shoppingCart= new ShoppingCart();

        return new TreeMap<>();
    }

    public ShoppingCart getShoppingCartUserById(int idUser){

        return initialShoppingCart().get(idUser);

    }
    // Método genérico para eliminar elementos de una lista en Java 8
    public static <DetailShoppingCart> List<DetailShoppingCart> remove(List<DetailShoppingCart> list, Predicate<DetailShoppingCart> predicate)
    {
        return list.stream()
                .filter(x -> !predicate.test(x))
                .collect(Collectors.toCollection(ArrayList::new));
    }


}
