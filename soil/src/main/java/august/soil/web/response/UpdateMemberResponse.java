package august.soil.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateMemberResponse {

    private Long member_id;
    private String loginId;
}
