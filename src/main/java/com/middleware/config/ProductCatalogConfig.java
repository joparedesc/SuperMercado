package com.middleware.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * ProductCatalogConfig config class.
 * @author jesu_
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "product-catalog")
public class ProductCatalogConfig {

    private List<Product> product;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {
        private int id;

        private String nombre;

        private String descripcion;

        private float precio;

        @Override
        public String toString(){
            StringBuffer sb = new StringBuffer();
            String ls = System.lineSeparator();
            sb.append(ls);
            sb.append("id:  " + id);
            sb.append(ls);
            sb.append("nombre:  " + nombre);
            sb.append(ls);
            sb.append("descripcion:  " + descripcion);
            sb.append(ls);
            sb.append("precio:  " + precio);
            sb.append(ls);
            return sb.toString();
        }


    }

}
