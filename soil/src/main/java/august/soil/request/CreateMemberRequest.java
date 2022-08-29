package august.soil.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateMemberRequest {

    @NotEmpty
    private String loginId;
}
