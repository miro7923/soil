package august.soil.service;

import august.soil.domain.Category;
import august.soil.domain.Diary;
import august.soil.domain.Member;
import august.soil.repository.DiaryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DiaryServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    DiaryService diaryService;
    @Autowired
    DiaryRepository diaryRepository;

    @Test
    public void 일기등록() throws Exception {
        // given
        Member member = memberService.findMember(5L);
        Category category = categoryService.findCategory(1L);
        Diary diary = new Diary(member, category, "fifth buy", "fifth diary", 5000);

        // when
        Long uploadId = diaryService.upload(diary);

        // then
        Assertions.assertEquals(diary, diaryRepository.findOne(uploadId));
    }

    @Test
    public void 일기목록조회() throws Exception {
        // given
        List<Diary> diaries = diaryService.findDiaries(5L);

        // when
        for (Diary d : diaries) {
            System.out.println(d.toString());
        }

        // then
    }
}