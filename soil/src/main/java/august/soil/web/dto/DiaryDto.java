package august.soil.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DiaryDto {

    @NotEmpty
    private String category;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotEmpty
    private LocalDateTime date;
    private String photo;
    @NotEmpty
    private int price;
}
