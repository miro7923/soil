package august.soil.dto;

import august.soil.domain.Category;
import august.soil.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DiaryDto {

    private String category;
    private String title;
    private String content;
    private LocalDateTime date;
    private String photo;
    private int price;
}
