package com.crud.crudfrontendbackend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuyProductDto {

    private String productName;

    private String seller;

    private int quantity;

    private String classify;
}
