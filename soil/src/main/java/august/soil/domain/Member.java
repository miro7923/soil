package august.soil.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    public Member(String loginId) {
        this.loginId = loginId;
    }

    @Column(name = "login_id")
    private String loginId;
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }
}
