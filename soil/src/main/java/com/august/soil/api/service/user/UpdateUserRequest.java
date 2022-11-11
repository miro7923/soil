package com.august.soil.api.service.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateUserRequest {

    @NotEmpty
    private String password;
}
