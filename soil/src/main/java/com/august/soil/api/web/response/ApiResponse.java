package com.august.soil.api.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    private T responseEntity;
    private int resultCode;
    private String msg;
}
