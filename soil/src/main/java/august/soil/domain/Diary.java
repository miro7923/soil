package august.soil.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor
public class Diary {

    @Id @GeneratedValue
    @Column(name = "diary_id")
    private Long id;

    public Diary(Member member, Category category, String title, String content, int price) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.price = price;
        this.date = LocalDateTime.now();
    }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;
    private String content;
    private LocalDateTime date;
    private String photo;
    private int price;
}
