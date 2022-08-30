package august.soil.web.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateMemberRequest {

    @NotEmpty
    private String password;
}
