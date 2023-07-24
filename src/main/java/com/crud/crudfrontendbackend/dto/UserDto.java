package com.crud.crudfrontendbackend.dto;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private String name;

    private String emailAddress;

    private String userName;
}
