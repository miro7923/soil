package august.soil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiariesResult<T> {

    private int count;
    private T diaryList;
}
