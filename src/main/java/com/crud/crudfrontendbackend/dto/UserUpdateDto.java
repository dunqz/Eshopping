package com.crud.crudfrontendbackend.dto;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateDto {

    private String name;

    private String userName;

}
