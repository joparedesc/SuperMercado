package com.middleware.DAO;

import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


public interface SaveProductDAO {
    CompletableFuture<String> sendMessageSaveProduct();
}
