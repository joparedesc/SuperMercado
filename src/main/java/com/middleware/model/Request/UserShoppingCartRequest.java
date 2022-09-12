package com.middleware.model.Request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserShoppingCartRequest {

    private int idProduct;

    private String nameProduct;

    @NotNull
    private int productQuantity;


}
