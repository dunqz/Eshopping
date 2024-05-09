package com.crud.crudfrontendbackend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Data
public class ProductFilter {

    private Long id;

    private String productName;

    private String seller;

    private Boolean isActive;

    private int stock;

    private int price;

    private String image;

    private String classify;

    public ProductFilter(Long id, String productName, String seller, Boolean isActive, int stock, int price, String image, String classify) {
        this.id = id;
        this.productName = productName;
        this.seller = seller;
        this.isActive = isActive;
        this.stock = stock;
        this.price = price;
        this.image = image;
        this.classify = classify;
    }

}
