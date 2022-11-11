package com.august.soil.api.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter
@AllArgsConstructor
public class GetDiaryResponse {

    private Long diary_id;
    private String category;
    private String title;
    private String content;
    private LocalDateTime date;
    private int price;
}
