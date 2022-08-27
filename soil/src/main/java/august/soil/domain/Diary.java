package august.soil.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
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
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime date;
    private String photo;
    private int price;
}
