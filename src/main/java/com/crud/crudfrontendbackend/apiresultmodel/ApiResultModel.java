package com.crud.crudfrontendbackend.apiresultmodel;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResultModel {

    private Object resultData;

    private String message;

    private boolean isSuccess;

    private int status;
}