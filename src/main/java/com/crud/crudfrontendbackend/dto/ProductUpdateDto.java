package com.crud.crudfrontendbackend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductUpdateDto {

    private String seller;

    private Boolean isActive;

    private int stock;

    private int price;
}
