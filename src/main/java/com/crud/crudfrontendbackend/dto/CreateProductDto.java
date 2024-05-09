package com.crud.crudfrontendbackend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProductDto {

    private Long id;

    private String productName;

    private String seller;

    private Boolean isActive;

    private int stock;

    private int price;

    private String classify;

}
