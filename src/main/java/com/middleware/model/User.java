package com.middleware.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private int idUser;

    private String name;

    private String lastNames;

    private String nickName;
}
