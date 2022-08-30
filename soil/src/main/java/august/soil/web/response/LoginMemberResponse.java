package august.soil.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class LoginMemberResponse {

    @NotEmpty
    private int resultCode;
    @NotEmpty
    private String msg;
}
