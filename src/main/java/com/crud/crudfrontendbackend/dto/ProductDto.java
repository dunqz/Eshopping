package com.crud.crudfrontendbackend.dto;

import lombok.*;

import javax.persistence.Column;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDto {

    private Long id;

    private String productName;

    private String supplierName;

    private String companyId;

    private Boolean isActive;

    private int stock;

    private int price;

    private String image;

}
