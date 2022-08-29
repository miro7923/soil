package august.soil.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginMemberRequest {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
}
