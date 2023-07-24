package com.crud.crudfrontendbackend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductUpdateDto {

    private String supplierName;

    private String companyId;

    private Boolean isActive;

    private int stock;

    private int price;
}
