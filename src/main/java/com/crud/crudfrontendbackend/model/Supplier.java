package com.crud.crudfrontendbackend.model;

import com.crud.crudfrontendbackend.auditable.Auditable;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Supplier extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64)
    private String supplierName;

    @Column(length = 2)
    private Boolean isDeleted;

    @Column(length = 2)
    private Boolean isActive;
}
