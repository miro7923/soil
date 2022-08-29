package august.soil.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogoutMemberResponse {

    private int resultCode;
    private String msg;
}
