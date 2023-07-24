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
public class Company extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64)
    private String companyName;

    @Column(length = 2)
    private Boolean isDeleted;

    @Column(length = 2)
    private Boolean isActive;
}
