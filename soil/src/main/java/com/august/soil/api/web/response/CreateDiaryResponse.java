package com.august.soil.api.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateDiaryResponse {

    private int resultCode;
    private String msg;
    private Long diary_id;
}
