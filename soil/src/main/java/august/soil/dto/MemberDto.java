package august.soil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDto {

    private Long member_id;
    private String loginId;
}
