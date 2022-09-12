package com.middleware.model;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShoppingCart {

    private int idShopping;

    private User user;

    private List<DetailShoppingCart> detailShoppingCart;

    private Double totalAmount;

    private Date dateShopping;


}
