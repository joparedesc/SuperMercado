package com.middleware.service.impl;

import com.middleware.model.Response.RestTemplateResponse;
import com.middleware.service.RestTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestTemplateServiceImplementation implements RestTemplateService {

    private final RestTemplate clientRest;

    @Value("${url-external.base-path}")
    private String urlBaseExternal;

    @Value("${url-external.api-post}")
    private String apiPosts;

    public List<RestTemplateResponse> getListResTemplateResponse(){
        List<RestTemplateResponse> restTemplateResponseList= Arrays.asList( clientRest.getForObject(urlBaseExternal+apiPosts,RestTemplateResponse[].class));
        return restTemplateResponseList;
    }



}
