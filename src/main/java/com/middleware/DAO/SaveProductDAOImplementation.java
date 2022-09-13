package com.middleware.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class SaveProductDAOImplementation implements SaveProductDAO{
    @Value("${async-config.sleep-time}")
    private int sleepTime;

    @Async("threadPoolExecutor")
    public CompletableFuture<String> sendMessageSaveProduct(){
        try {
            Thread.sleep(sleepTime);
            log.info("El producto ha sido agregado exitosamente.");
            return CompletableFuture.completedFuture("El producto ha sido agregado exitosamente.");
        } catch (InterruptedException ex) {
            log.error("Ha ocurrido un error ", ex);
            throw new RuntimeException(ex);
        }

    }

}
