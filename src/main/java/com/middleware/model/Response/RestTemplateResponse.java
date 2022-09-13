package com.middleware.model.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RestTemplateResponse {

    private int userId;

    private int id;

    private String title;

    private String body;

}
