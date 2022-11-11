package com.august.soil.api.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginMemberResponse {

    private String nickname;
    private String email;
}
