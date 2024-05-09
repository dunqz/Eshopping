package com.crud.crudfrontendbackend.model;

import com.crud.crudfrontendbackend.auditable.Auditable;
import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Product extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64)
    private String productName;

    @Column(length = 64)
    private String seller;

    @Column(length = 2)
    private Boolean isDeleted;

    @Column(length = 2)
    private Boolean isActive;

    @Column(name = "image")
    private String image;

    @Column(name = "stock")
    private int stock;

    @Column(name = "price")
    private int price;

    @Column(length = 64)
    private String classify;
}
