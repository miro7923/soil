package august.soil.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MemberResult {

    private int count;
    private List<MemberDto> memberList;
}
