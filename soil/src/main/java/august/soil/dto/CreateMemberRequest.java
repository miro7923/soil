package august.soil.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateMemberRequest {

    @NotEmpty
    private String loginId;
}
