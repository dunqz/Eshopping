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
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64)
    private String name;

    @Column(length = 64)
    private String emailAddress;

    @Column(length = 64)
    private String userName;

    @Column(length = 64)
    private String password;

    @Column(length = 64)
    private String companyId;

    @Column(length = 2)
    private Boolean isDeleted;

    @Column(length = 2)
    private Boolean isActive;

}
